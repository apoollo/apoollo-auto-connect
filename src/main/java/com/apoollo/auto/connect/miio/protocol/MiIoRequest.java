/**
 * 
 */
package com.apoollo.auto.connect.miio.protocol;

import com.alibaba.fastjson2.JSON;
import com.apoollo.auto.connect.utils.ByteArrayUtils;
import com.apoollo.auto.connect.utils.EncryptionUtils;

/**
 * @author liuyulong
 * @since 2025-04-09
 */
public class MiIoRequest {

	private MiIoRequestPayload payload;
	private byte[] token;
	private int deviceId;
	private int unknown1;
	private int timestamp;

	public MiIoRequest(MiIoRequestPayload payload, byte[] token, int deviceId, int unknown1, int timestamp) {
		super();
		this.payload = payload;
		this.token = token;
		this.deviceId = deviceId;
		this.unknown1 = unknown1;
		this.timestamp = timestamp;
	}

	public MiIoRequest(MiIoRequestPayload payload, byte[] token, int deviceId, int timestamp) {
		this(payload, token, deviceId, 0, timestamp);
	}

	public static MiIoRequest buildHelloMiIoRequest() {
		return new MiIoRequest(null, new byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }, -1,
				-1, -1);
	}

	public byte[] buildMiIoProtocol() {

		short magicNumber = 0x2131;
		short packageBytesLength = 32;

		MiIoMessage miIoProtocol = new MiIoMessage();
		miIoProtocol.setMagicNumber(magicNumber);
		miIoProtocol.setPacketLength(packageBytesLength);
		miIoProtocol.setUnknown1(unknown1);
		miIoProtocol.setDeviceId(deviceId);
		miIoProtocol.setTimestamp(timestamp);
		if (null != payload) {
			byte[] key = EncryptionUtils.md5(token);
			byte[] iv = EncryptionUtils.md5(ByteArrayUtils.joins(key, token));
			byte[] encrypted = EncryptionUtils.encrypt(key, iv, JSON.toJSONBytes(payload));
			short length = (short) (packageBytesLength + miIoProtocol.getEncrypted().length);
			miIoProtocol.setEncrypted(encrypted);
			miIoProtocol.setPacketLength(length);
		}
		return miIoProtocol.build(token);
	}

	/**
	 * @return the payload
	 */
	public MiIoRequestPayload getPayload() {
		return payload;
	}

	/**
	 * @return the token
	 */
	public byte[] getToken() {
		return token;
	}

	/**
	 * @return the deviceId
	 */
	public int getDeviceId() {
		return deviceId;
	}

	/**
	 * @return the unknown1
	 */
	public int getUnknown1() {
		return unknown1;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

}
