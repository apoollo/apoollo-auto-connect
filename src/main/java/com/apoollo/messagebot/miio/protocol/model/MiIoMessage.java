/**
 * 
 */
package com.apoollo.messagebot.miio.protocol.model;

/**
 * https://github.com/OpenMiHome/mihome-binary-protocol/blob/master/doc/PROTOCOL.md
 * 
 * 数据包基本格式
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

public class MiIoMessage {

	// header
	protected short magicNumber;
	protected long packetLength;// 无符号16位
	protected int unknown1;
	protected int deviceId;
	protected long timestamp;// 无符号32位

	// hello is token , other is checksum
	protected byte[] checksumOrToken;

	// data
	protected byte[] encrypted;

	/**
	 * @return the magicNumber
	 */
	public short getMagicNumber() {
		return magicNumber;
	}

	/**
	 * @return the packetLength
	 */
	public long getPacketLength() {
		return packetLength;
	}

	/**
	 * @return the unknown1
	 */
	public int getUnknown1() {
		return unknown1;
	}

	/**
	 * @return the deviceId
	 */
	public int getDeviceId() {
		return deviceId;
	}

	/**
	 * @return the timestamp
	 */
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the checksumOrToken
	 */
	public byte[] getChecksumOrToken() {
		return checksumOrToken;
	}

	/**
	 * @return the encrypted
	 */
	public byte[] getEncrypted() {
		return encrypted;
	}

}
