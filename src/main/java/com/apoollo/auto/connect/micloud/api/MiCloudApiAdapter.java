/**
 * 
 */
package com.apoollo.auto.connect.micloud.api;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.apoollo.auto.connect.micloud.api.request.ServiceLoginAuth2Request;
import com.apoollo.auto.connect.micloud.api.response.ServiceLoginAuth2Response;
import com.apoollo.auto.connect.utils.EncryptionUtils;

/**
 * @author liuyulong
 * @since 2025-04-11
 */
@Component
public class MiCloudApiAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(MiCloudApiAdapter.class);

	private XiaomiAccountApi accountApi;

	public MiCloudApiAdapter(XiaomiAccountApi accountApi) {
		super();
		this.accountApi = accountApi;
	}

	public String serviceLoginSign(String userName) {
		Map<String, String> headers = MicloudApiUtils.HEADERS;
		Map<String, String> cookies = MicloudApiUtils.getUserIdCookies(userName);
		String response = accountApi.serviceLogin(headers, cookies, "xiaomiio", "true", MicloudApiUtils.OPTIONS);
		LOGGER.info("serviceLogin response: " + response);
		JSONObject jsonObject = JSON.parseObject(response.replace("&&&START&&&", ""));
		return jsonObject.getString("_sign");
	}

	public ServiceLoginAuth2Response serviceLoginAuth2(String userName, String password, String sign) {
		Map<String, String> headers = MicloudApiUtils.HEADERS;
		Map<String, String> cookies = MicloudApiUtils.COOKIES;
		ServiceLoginAuth2Request serviceLoginAuth2Request = new ServiceLoginAuth2Request();
		serviceLoginAuth2Request.set_json("true");
		serviceLoginAuth2Request.set_sign(sign);
		serviceLoginAuth2Request.setCallback("https://sts.api.io.mi.com/sts");
		serviceLoginAuth2Request.setQs("%3Fsid%3Dxiaomiio%26_json%3Dtrue");
		serviceLoginAuth2Request.setUser(userName);
		serviceLoginAuth2Request.setSid("xiaomiio");
		serviceLoginAuth2Request.setHash(EncryptionUtils.md5ToUpperHexString(password));
		String response = accountApi.serviceLoginAuth2(headers, cookies, serviceLoginAuth2Request,
				MicloudApiUtils.OPTIONS);
		LOGGER.info("serviceLoginAuth2 response: " + response);
		JSONObject jsonObject = JSON.parseObject(response.replace("&&&START&&&", ""));
		return jsonObject.toJavaObject(ServiceLoginAuth2Response.class);
	}

}
