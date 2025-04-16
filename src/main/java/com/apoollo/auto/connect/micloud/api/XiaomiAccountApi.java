/**
 * 
 */
package com.apoollo.auto.connect.micloud.api;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.apoollo.auto.connect.micloud.api.request.ServiceLoginAuth2Request;

import feign.Request.Options;

/**
 * @author liuyulong
 * @since 2025-03-25
 */
@FeignClient(name = "micloud-account", url = "https://account.xiaomi.com")
public interface XiaomiAccountApi {

	@GetMapping(path = "/pass/serviceLogin", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String serviceLogin(@RequestHeader Map<String, String> headers, @CookieValue Map<String, String> cookies,
			@RequestParam String sid, @RequestParam String _json, Options options);

	@PostMapping(path = "/pass/serviceLoginAuth2", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String serviceLoginAuth2(@RequestHeader Map<String, String> headers,
			@CookieValue Map<String, String> cookies, ServiceLoginAuth2Request serviceLoginAuth2Request,
			Options options);
	
}
