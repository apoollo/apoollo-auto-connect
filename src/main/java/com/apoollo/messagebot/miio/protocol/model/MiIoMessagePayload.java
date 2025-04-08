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
public class MiIoMessagePayload {

	private String id;// UNIX timestamp
	private String method;// miIO.config_router
	private MiIoMessagePayloadParams params;

}
