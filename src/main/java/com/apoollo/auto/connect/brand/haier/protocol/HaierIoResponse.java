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
public class HaierIoResponse extends HaierIoMessage {

	private static final Logger LOGGER = LoggerFactory.getLogger(HaierIoResponse.class);

	public HaierIoResponse(InputStream inputStream) {
		do {
			this.separator = readBytes(inputStream, 2);
		} while (!Arrays.equals(this.separator, new byte[] { -1, -1 }));
		this.length = readByte(inputStream);

		byte[] lengthBytes = readBytes(inputStream, this.length);
		this.flags = lengthBytes[0];// 0
		System.arraycopy(lengthBytes, 1, this.reservedSpace, 0, 5);// 1-5
		this.type = lengthBytes[6];// 6
		System.arraycopy(lengthBytes, 7, this.data, 0, lengthBytes.length - 8);
		this.checksum = lengthBytes[lengthBytes.length - 1];// last
		this.crc = readBytes(inputStream, 2);

		// TODO validate checksum
		// TODO validate crc
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
