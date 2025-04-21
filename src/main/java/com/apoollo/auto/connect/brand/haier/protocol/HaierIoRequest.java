/**
 * 
 */
package com.apoollo.auto.connect.brand.haier.protocol;

/**
 * @author liuyulong
 * @since 2025-04-21
 */
public class HaierIoRequest {

	private byte flags;
	private byte type;
	private byte[] data;

	/**
	 * @param flags
	 * @param type
	 * @param data
	 */
	public HaierIoRequest(boolean hasCrc, byte type, byte[] data) {
		super();
		this.flags = hasCrc ? HaierIoProtocol.HAS_CRC_FLAGS : HaierIoProtocol.NOT_CRC_FLAGS;
		this.type = type;
		this.data = data;
	}

	public byte[] buildHaierIoProtocol() {
		HaierIoProtocol haierIoMessage = new HaierIoProtocol();
		haierIoMessage.separator = HaierIoProtocol.DEFAULT_SEPARATOR;

		byte length = HaierIoProtocol.NO_DATA_PACKET_BYTE_LENGTH;
		if (null != this.data) {
			length += this.data.length;
		}
		haierIoMessage.length = length;

		haierIoMessage.flags = this.flags;
		haierIoMessage.reservedSpace = HaierIoProtocol.DEFAULT_RESERVED_SPACE;
		haierIoMessage.type = this.type;
		haierIoMessage.data = this.data;
		haierIoMessage.checksum = haierIoMessage.buildChecksum();
		if (haierIoMessage.hasCrc()) {
			haierIoMessage.crc = haierIoMessage.buildCrc();
		}
		return haierIoMessage.build();
	}
}
