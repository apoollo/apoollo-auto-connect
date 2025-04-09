/**
 * 
 */
package com.apoollo.auto.connect.utils;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author liuyulong
 * @since 2025-04-09
 */
public class InetAddressUtils {

	public static InetAddress getInetAddress(String ip) {
		try {
			return InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<InetAddress> listAllBroadcastAddresses() {
		List<InetAddress> broadcastList = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();

				try {
					if (networkInterface.isLoopback() || !networkInterface.isUp()) {
						continue;
					}
				} catch (SocketException e) {
					continue;
				}
				for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
					if (address == null) {
						continue;
					}
					InetAddress broadcast = address.getBroadcast();
					if (broadcast != null) {
						broadcastList.add(broadcast);
					}
				}
			}
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		return broadcastList;
	}

}
