/**
 * 
 */
package com.apoollo.auto.connect.brand.xiaomi.micloud.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2025-04-15
 */
@Getter
@Setter
public class GetDeviceListRequest {

	private String userId;
	private String serviceToken;
	private String locale;
	private String timezone;
	private String isDaylight;
	private String dstOffset;
	private String channel;
}
