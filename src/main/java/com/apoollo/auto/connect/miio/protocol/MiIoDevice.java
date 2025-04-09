/**
 * 
 */
package com.apoollo.auto.connect.miio.protocol;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.apoollo.auto.connect.miio.protocol.response.MiIoResponse;
import com.apoollo.auto.connect.socket.UdpSocketDevice;
import com.apoollo.auto.connect.utils.ByteArrayUtils;
import com.apoollo.auto.connect.utils.InetAddressUtils;

/**
 * @author liuyulong
 * @since 2025-04-03
 */

public class MiIoDevice extends UdpSocketDevice<MiIoResponse> {

	private UdpSocketDeviceRequest buildRequest(InetAddress inteAdress, boolean broadcast, int responseBufferLength,
			byte[] content) {
		return new UdpSocketDeviceRequest(socket -> {
			socket.setBroadcast(broadcast);
			socket.setSoTimeout(5);
		}, new DatagramPacket(content, content.length, inteAdress, 54321), responseBufferLength, 3);
	}

	public MiIoResponse hello(InetAddress inteAdress) {
		byte[] hello = ByteArrayUtils
				.hexStringToByteArray("21310020ffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
		return request(buildRequest(inteAdress, true, 1024, hello));
	}

	public List<MiIoResponse> discover() throws UnknownHostException {
		List<MiIoResponse> miioResponses = Stream
				.concat(InetAddressUtils.listAllBroadcastAddresses().stream(),
						Stream.of(InetAddressUtils.getInetAddress("255.255.255.255")))
				.map(inteAdress -> hello(inteAdress)).filter(Objects::nonNull).collect(Collectors.toList());
		return miioResponses;
	}

	public void action() {

	}

	@Override
	public MiIoResponse getResponse(DatagramPacket reponsePacket) {
		return new MiIoResponse(reponsePacket);
	}

}
