/**
 * 
 */
package com.apoollo.auto.connect.brand.haier.protocol;

import java.io.InputStream;

import com.apoollo.auto.connect.device.TcpSocketDevice;

/**
 * @author liuyulong
 * @since 2025-04-16
 */
public class HaierIoDevice extends TcpSocketDevice<HaierIoResponse> {

	@Override
	public HaierIoResponse getResponse(InputStream inputStream) {
		return new HaierIoResponse(inputStream);
	}

}
