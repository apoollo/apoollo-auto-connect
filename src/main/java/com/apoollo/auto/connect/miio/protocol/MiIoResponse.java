/**
 * 
 */
package com.apoollo.auto.connect.miio.protocol;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.apoollo.auto.connect.utils.ByteArrayUtils;
import com.apoollo.auto.connect.utils.EncryptionUtils;

/**
 * @author liuyulong
 * @since 2025-04-07
 */
public class MiIoResponse extends MiIoMessage {

	private static final Logger LOGGER = LoggerFactory.getLogger(MiIoResponse.class);

	private static final List<Function<byte[], byte[]>> BYTES_QUIRK_TRY_FILTERS = new ArrayList<>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 9116323702375395214L;

		{
			add(bytes -> bytes);
			add(bytes -> ByteArrayUtils.subbytesBefore(bytes, 0x00, 1));
		}

	};

	private static final List<Function<String, String>> STRING_QUIRK_TRY_FILTERS = new ArrayList<>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1349247499884515013L;
		{
			add(string -> string.replace(",,\"otu_stat\"", ",\"otu_stat\""));
			add(string -> string.replace("\"value\":00", "\"value\":0"));
			add(string -> string.replace(",,", ","));
			add(string -> string.replace("\"result\":,", ""));
		}
	};

	// address
	private String ip;
	private int port;

	// calculated header
	private String hexDeviceId;

	// calculated hello , hello is token , other is checksum
	private byte[] token;
	private String tokenString;

	// calculated none hello ,hello is token , other is checksum
	private byte[] checksum;

	public MiIoResponse(DatagramPacket datagramPacket) {

		byte[] data = datagramPacket.getData();

		ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		// address info
		this.ip = (datagramPacket.getAddress().getHostAddress());
		this.port = (datagramPacket.getPort());

		// Magic number: 16 bits
		this.magicNumber = (byteBuffer.getShort());

		// Packet length: 16 bits unsigned int
		this.packetLength = byteBuffer.getShort();

		// Unknown1: 32 bits
		this.unknown1 = (byteBuffer.getInt());

		// Device ID: 32 bits
		this.deviceId = (byteBuffer.getInt());
		this.hexDeviceId = Integer.toHexString(this.deviceId);

		// Stamp: 32 bit unsigned int
		this.timestamp = byteBuffer.getInt();

		// 128-bit checksumOrToken
		byte[] checksumOrToken = new byte[32];
		byteBuffer.get(checksumOrToken);

		// hello
		if (32 == this.packetLength) {
			this.token = checksumOrToken;
			this.tokenString = ByteArrayUtils.toHexString(checksumOrToken);
		}

		// non hello
		// encrypted data: length = packet-length - 0x20
		if (byteBuffer.hasRemaining()) {

			this.checksum = checksumOrToken;
			this.encrypted = new byte[byteBuffer.remaining()];
			byteBuffer.get(encrypted);
		}

	}

	// valid checksum
	public boolean checksum(byte[] token) {
		return null != this.checksum && Arrays.equals(EncryptionUtils.md5(this.build(token)), this.checksum);
	}

	public JSONObject getDecrypted(byte[] key, byte[] iv) {
		return getDecrypted(key, iv, BYTES_QUIRK_TRY_FILTERS, STRING_QUIRK_TRY_FILTERS);
	}

	public JSONObject getDecrypted(byte[] key, byte[] iv, List<Function<byte[], byte[]>> bytesQuirksTryFilters,
			List<Function<String, String>> stringQuirksTryFilters) {

		JSONObject decypted = null;
		if (null != encrypted) {

			byte[] plaintextBytes = EncryptionUtils.decrypt(key, iv, encrypted);

			byte[] rstripedBytes = ByteArrayUtils.rstrip(plaintextBytes, 0x00);

			String payload = new String(rstripedBytes, StandardCharsets.UTF_8);

			LOGGER.info("response payload: " + payload);

			// process quirks
			if (null != bytesQuirksTryFilters) {
				decypted = bytesQuirksTryFilters.stream().map(filter -> {

					try {
						return JSON.parseObject(filter.apply(rstripedBytes));
					} catch (Exception e) {
						return null;
					}

				}).filter(Objects::nonNull).findFirst().orElse(null);
			}

			if (null != stringQuirksTryFilters && null == decypted) {

				decypted = stringQuirksTryFilters.stream().map(filter -> {

					try {
						return JSON.parseObject(filter.apply(payload));
					} catch (Exception e) {
						return null;
					}
				}).filter(Objects::nonNull).findFirst().orElse(null);

			}

			if (null == decypted) {
				throw new RuntimeException("can't parse to json : " + payload);
			}
		}

		return decypted;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the hexDeviceId
	 */
	public String getHexDeviceId() {
		return hexDeviceId;
	}

	/**
	 * @return the checksumOrToken
	 */
	public byte[] getChecksum() {
		return checksum;
	}

	/**
	 * @return the token
	 */
	public byte[] getToken() {
		return token;
	}

	/**
	 * @return the token
	 */
	public String getTokenString() {
		return tokenString;
	}

}
