/**
 * 
 */
package com.apoollo.auto.connect.brand.xiaomi.micloud.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2025-04-10
 */
@Getter
@Setter
public class ServiceLoginAuth2Request {

	private String sid;
	private String hash;
	private String callback;
	private String qs;
	private String user;
	private String _json;
	private String _sign;
}
