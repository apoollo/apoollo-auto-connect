/**
 * 
 */
package com.apoollo.auto.connect.brand.haier.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuyulong
 * @since 2025-04-18
 */
public class HaierIoResponse extends HaierIoProtocol {

	private static final Logger LOGGER = LoggerFactory.getLogger(HaierIoResponse.class);

	public HaierIoResponse(InputStream inputStream) {
		do {
			this.separator = readBytes(inputStream, 2);
		} while (!Arrays.equals(this.separator, HaierIoProtocol.DEFAULT_SEPARATOR));

		this.length = readByte(inputStream);
		byte[] lengthBytes = readBytes(inputStream, this.length);

		this.flags = lengthBytes[0];// 0

		this.reservedSpace = new byte[5];
		System.arraycopy(lengthBytes, 1, this.reservedSpace, 0, this.reservedSpace.length);// 1-5

		this.type = lengthBytes[6];// 6

		if (HaierIoProtocol.NO_DATA_PACKET_BYTE_LENGTH < lengthBytes.length) {
			this.data = new byte[lengthBytes.length - HaierIoProtocol.NO_DATA_PACKET_BYTE_LENGTH];
			System.arraycopy(lengthBytes, 7, this.data, 0, this.data.length);
		}

		this.checksum = lengthBytes[lengthBytes.length - 1];// last

		// validate checksum
		if (this.checksum != this.buildChecksum()) {
			throw new RuntimeException("checksum validate failed : " + this.checksum);
		}

		// validate crc
		if (this.hasCrc()) {
			this.crc = readBytes(inputStream, 2);
			if (!Arrays.equals(this.crc, this.buildCrc())) {
				throw new RuntimeException("crc validate failed");
			}
		}

	}

	private byte readByte(InputStream inputStream) {
		return readBytes(inputStream, 1)[0];
	}

	private byte[] readBytes(InputStream inputStream, int length) {
		byte[] bytes = null;
		try {
			bytes = inputStream.readNBytes(length);
			if (bytes.length != length) {
				throw new RuntimeException("readNBytes length not match");
			}
		} catch (IOException e) {
			LOGGER.error("readNBytes error", e);
			throw new RuntimeException(e);
		}
		return bytes;
	}

}
