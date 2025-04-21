/**
 * 
 */
package com.apoollo.auto.connect.brand.xiaomi.protocol;

import java.nio.ByteBuffer;

import com.apoollo.auto.connect.utils.ByteArrayUtils;

/**
 * 
 * <h1>数据包基本格式</h1>
 * https://github.com/OpenMiHome/mihome-binary-protocol/blob/master/doc/PROTOCOL.md
 * 
 * <pre>
 0                   1                   2                   3   
 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 
+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
| Magic number = 0x2131         | Packet Length (incl. header)  |
|-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-|
| Unknown1                                                      |
|-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-|
| Device ID ("did")                                             |
|-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-|
| Stamp                                                         |
|-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-|
| MD5 checksum                                                  |
| ... or Device Token in response to the "Hello" packet         |
|-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-|
| optional variable-sized data (encrypted)                      |
|...............................................................|

                Mi Home Binary Protocol header
       Note that one tick mark represents one bit position.
 
 Magic number: 16 bits
     Always 0x2131
     
 Packet length: 16 bits unsigned int
     Length in bytes of the whole packet, including the header.
  
 Unknown1: 32 bits
     This value is always 0,
     except in the "Hello" packet, when it's 0xFFFFFFFF
     
 Device ID: 32 bits
     Unique number. Possibly derived from the MAC address.
     except in the "Hello" packet, when it's 0xFFFFFFFF

 Stamp: 32 bit unsigned int
     continously increasing counter
     
 MD5 checksum:
     calculated for the whole packet including the MD5 field itself,
     which must be initialized with 0.
     
     In the special case of the response to the "Hello" packet,
     this field contains the 128-bit device token instead.
 
 optional variable-sized data:
     encrypted with AES-128: see below.
     length = packet_length - 0x20
 * </pre>
 * 
 * @author liuyulong
 * @since 2025-04-02
 */

public class MiIoProtocol {

	// header
	protected short magicNumber;
	protected short packetLength;// 无符号16位
	protected int unknown1;
	protected int deviceId;
	protected int timestamp;// 无符号32位

	// data
	protected byte[] encrypted;

	private byte[] buildHeaderBytes() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(16);
		byteBuffer.putShort(magicNumber);
		byteBuffer.putShort(packetLength);
		byteBuffer.putInt(unknown1);
		byteBuffer.putInt(deviceId);
		byteBuffer.putInt(timestamp);
		return byteBuffer.array();
	}

	public byte[] build(byte[] token) {
		return ByteArrayUtils.joins(buildHeaderBytes(), token, encrypted);
	}

	/**
	 * @return the magicNumber
	 */
	public short getMagicNumber() {
		return magicNumber;
	}

	/**
	 * @param magicNumber the magicNumber to set
	 */
	public void setMagicNumber(short magicNumber) {
		this.magicNumber = magicNumber;
	}

	/**
	 * @return the packetLength
	 */
	public short getPacketLength() {
		return packetLength;
	}

	/**
	 * @param packetLength the packetLength to set
	 */
	public void setPacketLength(short packetLength) {
		this.packetLength = packetLength;
	}

	/**
	 * @return the unknown1
	 */
	public int getUnknown1() {
		return unknown1;
	}

	/**
	 * @param unknown1 the unknown1 to set
	 */
	public void setUnknown1(int unknown1) {
		this.unknown1 = unknown1;
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
	public int getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the encrypted
	 */
	public byte[] getEncrypted() {
		return encrypted;
	}

	/**
	 * @param encrypted the encrypted to set
	 */
	public void setEncrypted(byte[] encrypted) {
		this.encrypted = encrypted;
	}

}
