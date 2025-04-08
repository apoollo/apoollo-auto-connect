/**
 * 
 */
package com.apoollo.messagebot.miio.protocol.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2025-04-03
 */
@Getter
@Setter
public class MiIoMessagePayloadParams {

	private String ssid;// WiFi network
	private String passwd;// WiFi password
	private String uid;// identifies the device owner. The device will phone home and report this to
						// Xiaomi.
}
