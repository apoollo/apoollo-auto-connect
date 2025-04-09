/**
 * 
 */
package com.apoollo.auto.connect.miio.protocol.request;

import com.alibaba.fastjson2.JSON;
import com.apoollo.auto.connect.miio.protocol.model.MiIoProtocol;
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
	private long timestamp;

	public MiIoRequest() {
	}

	public MiIoRequest(MiIoRequestPayload payload, byte[] token, int deviceId, long timestamp) {
		super();
		this.payload = payload;
		this.token = token;
		this.deviceId = deviceId;
		this.timestamp = timestamp;
	}

	public byte[] buildMiIoProtocol() {
		MiIoProtocol miIoProtocol = new MiIoProtocol();
		miIoProtocol.setMagicNumber((short) 0x2131);
		miIoProtocol.setPacketLength(32);
		miIoProtocol.setUnknown1(0);
		miIoProtocol.setDeviceId(deviceId);
		miIoProtocol.setTimestamp(timestamp);
		if (null != payload) {
			byte[] key = EncryptionUtils.md5(token);
			byte[] iv = EncryptionUtils.md5(ByteArrayUtils.join(key, token));
			miIoProtocol
					.setEncrypted(EncryptionUtils.encrypt(EncryptionUtils.md5(token), iv, JSON.toJSONBytes(payload)));
			miIoProtocol.setPacketLength(miIoProtocol.getPacketLength() + miIoProtocol.getEncrypted().length);
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
	 * @param payload the payload to set
	 */
	public void setPayload(MiIoRequestPayload payload) {
		this.payload = payload;
	}

	/**
	 * @return the token
	 */
	public byte[] getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(byte[] token) {
		this.token = token;
	}

	/**
	 * @return the deviceId
	 */
	public int getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
