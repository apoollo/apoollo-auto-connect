/**
 * 
 */
package com.apoollo.auto.connect.brand.haier.protocol;

import java.io.InputStream;

import com.apoollo.auto.connect.device.TcpSocketDevice;
import com.apoollo.auto.connect.utils.ByteArrayUtils;

/**
 * @author liuyulong
 * @since 2025-04-16
 */
public class HaierIoDevice extends TcpSocketDevice<HaierIoResponse> {

	@Override
	public HaierIoResponse getResponse(InputStream inputStream) {
		return new HaierIoResponse(inputStream);
	}

	public HaierIoResponse request(String host, int port, byte[] data) {
		return request(host, port, socket -> {
			socket.setSoTimeout(5000);
		}, data);
	}

	public HaierIoResponse request(String host, int port, HaierIoRequest haierIoRequest) {
		return request(host, port, haierIoRequest.buildHaierIoProtocol());
	}

	public HaierIoResponse hello(String host, int port) {
		// ffff0a000000000000014d0159
		return request(host, port,
				new HaierIoRequest(false, HaierIoProtocolType.CONTROL.getCode(), new byte[] { 77, 1 })
						.buildHaierIoProtocol());
	}

	public HaierIoResponse on(String host, int port) {
		// ffff0a000000000000014d025a
		return request(host, port,
				new HaierIoRequest(false, HaierIoProtocolType.CONTROL.getCode(), new byte[] { 77, 2 })
						.buildHaierIoProtocol());
	}

	public HaierIoResponse of(String host, int port) {
		// ffff0a000000000000014d035b
		return request(host, port,
				new HaierIoRequest(false, HaierIoProtocolType.CONTROL.getCode(), new byte[] { 77, 3 })
						.buildHaierIoProtocol());
	}

	public static void main(String[] args) {

		// 56800

		new HaierIoResponse(ByteArrayUtils.hexStringTo("ffff0a000000000000014d0159"));// hello
		new HaierIoResponse(ByteArrayUtils.hexStringTo("ffff0a000000000000014d025a"));// on
		new HaierIoResponse(ByteArrayUtils.hexStringTo("ffff0a000000000000014d035b"));// of
		new HaierIoResponse(ByteArrayUtils.hexStringTo("ffff08000000000000737b"));// init
	}

}
