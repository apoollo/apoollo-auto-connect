/**
 * 
 */
package com.apoollo.messagebot.miio.protocol.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.messagebot.miio.protocol.MiIoResponse;

/**
 * @author liuyulong
 * @since 2025-04-07
 */
public class ProtocolUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolUtils.class);

	private static final int DEFAULT_PORT = 54321;

	private static final byte[] REQUEST_HELLO_BYTES = Hex
			.decodeStrict("21310020ffffffffffffffffffffffffffffffffffffffffffffffffffffffff");

	public static List<MiIoResponse> discover() {

		// default discover params
		int helloReponseBytesLength = 1024;
		int socketTimeout = 10000;
		int limitTryTimes = 3;
		int tryTimes = 0;

		// auto discover ip
		List<MiIoResponse> miioResponses = listAllBroadcastAddresses().stream()
				.map(inteAdress -> request(true, socketTimeout,
						new DatagramPacket(REQUEST_HELLO_BYTES, REQUEST_HELLO_BYTES.length, inteAdress, DEFAULT_PORT),
						helloReponseBytesLength, limitTryTimes, tryTimes))
				.filter(Objects::nonNull).collect(Collectors.toList());

		// assigned broadcast ip
		MiIoResponse miioResponse = request(true, socketTimeout,
				new DatagramPacket(REQUEST_HELLO_BYTES, REQUEST_HELLO_BYTES.length,
						new InetSocketAddress("255.255.255.255", DEFAULT_PORT)),
				helloReponseBytesLength, limitTryTimes, tryTimes);
		if (null != miioResponse) {
			miioResponses.add(miioResponse);
		}
		return miioResponses;
	}

	public static MiIoResponse request(boolean broadcast, int socketTimeout, DatagramPacket requestPacket,
			int reponseBytesLength, int limitTryTimes, int tryTimes) {
		MiIoResponse miioResponse = null;
		try (DatagramSocket datagramSocket = new DatagramSocket()) {
			datagramSocket.setBroadcast(broadcast);
			datagramSocket.setSoTimeout(socketTimeout);
			datagramSocket.send(requestPacket);
			byte[] buffer = new byte[reponseBytesLength];
			DatagramPacket reponsePacket = new DatagramPacket(buffer, buffer.length);
			datagramSocket.receive(reponsePacket);
			miioResponse = new MiIoResponse(reponsePacket);
		} catch (IOException e) {
			LOGGER.debug("try request error : ", e);
			if (tryTimes < limitTryTimes) {
				miioResponse = request(broadcast, socketTimeout, requestPacket, reponseBytesLength, limitTryTimes,
						++tryTimes);
			}
		} catch (Exception e) {
			throw e;
		}
		return miioResponse;
	}

	public static byte[] subbytesBefore(byte[] bytes, int filter) {
		int i = 0;
		for (; i < bytes.length; i++) {
			if (bytes[i] == filter) {
				break;
			}
		}
		if (i != bytes.length) {
			byte[] subbytesed = new byte[i];
			System.arraycopy(bytes, 0, subbytesed, 0, subbytesed.length);
			bytes = subbytesed;
		}
		return bytes;
	}

	public static byte[] rstrip(byte[] bytes, int filter) {
		// right strip 0
		int i = bytes.length - 1;
		for (; i >= 0; i--) {
			if (bytes[i] != filter) {
				break;
			}
		}
		if (i > -1) {
			byte[] lstriped = new byte[i + 1];
			System.arraycopy(bytes, 0, lstriped, 0, lstriped.length);
			bytes = lstriped;
		}

		return bytes;
	}

	public static byte[] hexStringToByteArray(String hex) {
		int len = hex.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}
		return data;
	}

	public static List<InetAddress> listAllBroadcastAddresses() {
		List<InetAddress> broadcastList = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();

				try {
					if (networkInterface.isLoopback() || !networkInterface.isUp()) {
						continue;
					}
				} catch (SocketException e) {
					continue;
				}
				for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
					if (address == null) {
						continue;
					}
					InetAddress broadcast = address.getBroadcast();
					if (broadcast != null) {
						broadcastList.add(broadcast);
					}
				}
			}
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		return broadcastList;
	}

	public static byte[] md5(byte[] bs) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bs);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] join(byte[] array1, byte[] array2) {
		byte[] result = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, result, 0, array1.length);
		System.arraycopy(array2, 0, result, array1.length, array2.length);
		return result;
	}

	public static byte[] decrypt(byte[] key, byte[] iv, byte[] input) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			IvParameterSpec params = new IvParameterSpec(iv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
			return cipher.doFinal(input);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

}
