/**
 * 
 */
package com.apoollo.auto.connect.micloud.api.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2025-04-11
 */
@Getter
@Setter
public class ServiceLoginAuth2Response {

	private String notificationUrl;
	private String captchaUrl;
	private String description;
	private Integer securityStatus;
	private String pwd;
	private String child;
	private String  desc;
	private String result;
	
	private String userId;
	private String ssecurity;
	private String cUserId;
	private String passToken;
	private String location;
	private Integer code;
}
