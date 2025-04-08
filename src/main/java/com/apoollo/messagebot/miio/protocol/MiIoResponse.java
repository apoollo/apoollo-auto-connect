/**
 * 
 */
package com.apoollo.messagebot.miio.protocol;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.bouncycastle.util.encoders.Hex;

import com.apoollo.messagebot.miio.protocol.model.MiIoMessage;
import com.apoollo.messagebot.miio.protocol.utils.ProtocolUtils;

/**
 * @author liuyulong
 * @since 2025-04-07
 */
public class MiIoResponse extends MiIoMessage {

	// address
	private String ip;
	private int port;

	// calculated header
	private String hexDeviceId;

	// calculated hello
	private byte[] tokenMd5;
	private byte[] tokenIv;
	private String token;

	public MiIoResponse(DatagramPacket datagramPacket) {

		ByteBuffer byteBuffer = ByteBuffer.wrap(datagramPacket.getData());
		// address info
		this.ip = (datagramPacket.getAddress().getHostAddress());
		this.port = (datagramPacket.getPort());

		// Magic number: 16 bits
		this.magicNumber = (byteBuffer.getShort());

		// Packet length: 16 bits unsigned int
		this.packetLength = Integer.toUnsignedLong(byteBuffer.getShort());

		// Unknown1: 32 bits
		this.unknown1 = (byteBuffer.getInt());

		// Device ID: 32 bits
		this.deviceId = (byteBuffer.getInt());
		this.hexDeviceId = Integer.toHexString(this.deviceId);

		// Stamp: 32 bit unsigned int
		this.timestamp = Integer.toUnsignedLong(byteBuffer.getInt());

		// 128-bit checksumOrToken
		byte[] checksumOrToken = new byte[32];
		byteBuffer.get(checksumOrToken);
		this.checksumOrToken = (checksumOrToken);

		// hello
		if (32 == this.packetLength) {
			this.tokenMd5 = ProtocolUtils.md5(checksumOrToken);
			this.tokenIv = ProtocolUtils.md5(ProtocolUtils.join(this.tokenMd5, checksumOrToken));
			this.token = Hex.toHexString(checksumOrToken);
		}
		
		// non hello
		// TODO valid checksum
		
		// encrypted data: length = packet-length - 0x20
		if (byteBuffer.hasRemaining()) {
			this.encrypted = new byte[byteBuffer.remaining()];
			byteBuffer.get(encrypted);
		}

	}

	public String getDecrypted(byte[] key, byte[] iv) {

		String decypted = null;
		if (null != encrypted) {
			byte[] bytes = ProtocolUtils.decrypt(key, iv, encrypted);
			bytes = ProtocolUtils.rstrip(bytes, 0x00);

			// TODO process quirks

			decypted = new String(bytes, StandardCharsets.UTF_8);
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
	 * @return the checksumMd5
	 */
	public byte[] getChecksumMd5() {
		return tokenMd5;
	}

	/**
	 * @return the iv
	 */
	public byte[] getIv() {
		return tokenIv;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the hexDeviceId
	 */
	public String getHexDeviceId() {
		return hexDeviceId;
	}

}
