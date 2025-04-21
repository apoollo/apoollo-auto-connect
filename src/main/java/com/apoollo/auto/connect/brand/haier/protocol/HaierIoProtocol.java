/**
 * 
 */
package com.apoollo.auto.connect.brand.haier.protocol;

import java.nio.ByteBuffer;

import com.apoollo.auto.connect.utils.ByteArrayUtils;
import com.apoollo.auto.connect.utils.Crc16ArcUtils;

/**
 * <h1>数据包基本格式</h1>
 * 
 * https://github.com/paveldn/HaierProtocol/tree/main
 * 
 * <pre>
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   | Frame separator | Frame length | Frame flags | Reserved space | Type   | Frame data         | Checksum | CRC      |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   | 2 bytes         | 1 byte	     | 1 byte	   | 5 bytes	    | 1 byte | n bytes (n <= 246) | 1 byte	 | 2 bytes  |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   Where:
	Frame separator - 2 bytes, have fixed value 0xFF 0xFF, used as a marker for the beginning of the frame
	Frame length - 1 byte, number of bytes of the entire frame, includes frame flags, reserved space, type byte, frame data, and checksum, max value is 254 *
	Frame flags - 1 byte, only 2 values used 0x40 - indicates that frame have CRC bytes, 0x00 - indicates that there is no CRC
	Reserved space - 5 bytes, reserved for future use, filled with 0x00
	Frame type - 1 byte, type of frame (depend on the protocol)
	Frame data - n byte, data of the frame, can be empty. Sometimes first 2 bytes of data are used as a subcommand. Max size 246
	Checksum - 1 byte, the least significant byte of the sum of all bytes of the frame except separator bytes, CRC, and checksum itself.
	CRC - 2 bytes, CRC 16 of all bytes of the frame except separator bytes, checksum byte, and CRC itself (CRC-16/ARC algorithm used). CRC is available only if the frame flags byte indicates it.
 * </pre>
 * 
 * 
 * @author liuyulong
 * @since 2025-04-18
 */
public class HaierIoProtocol {

	public static final byte[] DEFAULT_SEPARATOR = { -1, -1 };

	public static final byte[] DEFAULT_RESERVED_SPACE = { 0, 0, 0, 0, 0 };

	public static final byte NO_DATA_PACKET_BYTE_LENGTH = 8;// flags + reservedSpace + type + checksum

	private static final byte NO_DATA_CHECK_BYTE_LENGTH = 8;// length + flags + reservedSpace + type

	public static final byte HAS_CRC_FLAGS = 0x40;

	public static final byte NOT_CRC_FLAGS = 0x00;

	protected byte[] separator;// 16位，分隔符，用作数据包的起始标记
	protected byte length;// 8位，整个包的字节数，最大字节数254，包含flags、reservedSpace、type、data、checksum
	protected byte flags;// 8位，是否包含CRC，0x40 表示包含，0x00 表示不包含
	protected byte[] reservedSpace;// 40位 保留字段供将来使用，以0x00填充
	protected byte type;// 8位，数据包类型，取决于协议，可能跟业务相关
	protected byte[] data;// 可变长度，数据包业务数据，最大字节246，可以为空，有时后，数据的前2个字节会用作子命令
	protected byte checksum;// 8位，数据包中除了separator、checksum、crc之外的所有字节之和的最低有效字节。
	protected byte[] crc;// 16位，数据包中除了separator、checksum、crc之外的所有字节的CRC
							// 16（使用CRC-16/ARC算法）。只有当flags指示CRC时，CRC才可用。

	public byte[] getCheckBytes() {
		int checkLength = NO_DATA_CHECK_BYTE_LENGTH;
		if (null != this.data) {
			checkLength += this.data.length;
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(checkLength);
		byteBuffer.put(this.length);
		byteBuffer.put(this.flags);
		byteBuffer.put(this.reservedSpace);
		byteBuffer.put(type);
		if (null != this.data) {
			byteBuffer.put(this.data);
		}
		return byteBuffer.array();
	}

	public byte buildChecksum() {
		return ByteArrayUtils.unsignedLongSumToByte(this.getCheckBytes());
	}

	public byte[] buildCrc() {
		short crc = Crc16ArcUtils.crc16(getCheckBytes());
		ByteBuffer byteBuffer = ByteBuffer.allocate(2);
		return byteBuffer.putShort(crc).array();
	}

	public boolean hasCrc() {
		return this.flags == HAS_CRC_FLAGS;
	}

	public byte[] build() {
		byte[] messageBytes = ByteArrayUtils.joins(this.separator, getCheckBytes(), new byte[] { this.checksum },
				hasCrc() ? this.crc : null);
		if (messageBytes.length > 254) {
			throw new RuntimeException("haier message protocol max length is 254 , current is :" + messageBytes.length);
		}
		return messageBytes;

	}

	/**
	 * @return the separator
	 */
	public byte[] getSeparator() {
		return separator;
	}

	/**
	 * @return the length
	 */
	public byte getLength() {
		return length;
	}

	/**
	 * @return the flags
	 */
	public byte getFlags() {
		return flags;
	}

	/**
	 * @return the reservedSpace
	 */
	public byte[] getReservedSpace() {
		return reservedSpace;
	}

	/**
	 * @return the type
	 */
	public byte getType() {
		return type;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @return the checksum
	 */
	public byte getChecksum() {
		return checksum;
	}

	/**
	 * @return the crc
	 */
	public byte[] getCrc() {
		return crc;
	}

}
