/**
 * 
 */
package com.apoollo.auto.connect.device;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apoollo.auto.connect.model.ThrowingConsumer;

/**
 * @author liuyulong
 * @since 2025-04-09
 */
public abstract class UdpSocketDevice<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(UdpSocketDevice.class);

	private T send(DatagramSocket datagramSocket, DatagramPacket requestPacket, int responseBufferLength,
			int limitTryTimes, int tryTimes) {
		T response = null;
		try {
			datagramSocket.send(requestPacket);
			byte[] buffer = new byte[responseBufferLength];
			DatagramPacket reponsePacket = new DatagramPacket(buffer, buffer.length);
			datagramSocket.receive(reponsePacket);
			response = getResponse(reponsePacket);
		} catch (IOException e) {
			LOGGER.error("udp socket send error : ", e);
			if (tryTimes < limitTryTimes) {
				response = send(datagramSocket, requestPacket, responseBufferLength, limitTryTimes, ++tryTimes);
			}
		}
		return response;
	}

	public T request(UdpSocketDeviceRequest request) {
		T response = null;
		try (DatagramSocket datagramSocket = new DatagramSocket()) {
			try {
				request.getConfigSocket().acceptWithException(datagramSocket);
			} catch (SocketException e) {
				LOGGER.error("udp socket config error : ", e);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			response = send(datagramSocket, request.getRequestPacket(), request.getResponseBufferLength(),
					request.getLimitTryTimes(), 0);
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	public abstract T getResponse(DatagramPacket reponsePacket);

	/**
	 * @author liuyulong
	 * @since 2025-04-09
	 */
	public static class UdpSocketDeviceRequest {
		private ThrowingConsumer<DatagramSocket> configSocket;
		private DatagramPacket requestPacket;
		private int responseBufferLength;
		private int limitTryTimes;

		public UdpSocketDeviceRequest(ThrowingConsumer<DatagramSocket> configSocket, DatagramPacket requestPacket,
				int responseBufferLength, int limitTryTimes) {
			super();
			this.configSocket = configSocket;
			this.requestPacket = requestPacket;
			this.responseBufferLength = responseBufferLength;
			this.limitTryTimes = limitTryTimes;
		}

		/**
		 * @return the configSocket
		 */
		public ThrowingConsumer<DatagramSocket> getConfigSocket() {
			return configSocket;
		}

		/**
		 * @return the requestPacket
		 */
		public DatagramPacket getRequestPacket() {
			return requestPacket;
		}

		/**
		 * @return the responseBufferLength
		 */
		public int getResponseBufferLength() {
			return responseBufferLength;
		}

		/**
		 * @return the limitTryTimes
		 */
		public int getLimitTryTimes() {
			return limitTryTimes;
		}

	}
}
