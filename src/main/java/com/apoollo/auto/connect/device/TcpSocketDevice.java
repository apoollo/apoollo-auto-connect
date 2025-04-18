/**
 * 
 */
package com.apoollo.auto.connect.device;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author liuyulong
 * @since 2025-04-16
 */
public abstract class TcpSocketDevice<T> {

	public T request(String host, int port, byte[] data) {
		try (Socket socket = new Socket(host, port)) {
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(data);
			outputStream.flush();
			InputStream inputStream = socket.getInputStream();
			return getResponse(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public abstract T getResponse(InputStream inputStream);
}
