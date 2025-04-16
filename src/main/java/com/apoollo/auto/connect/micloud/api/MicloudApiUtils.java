/**
 * 
 */
package com.apoollo.auto.connect.micloud.api;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.auto.connect.micloud.api.request.GetDeviceListRequest;
import com.apoollo.commons.util.OkHttpClientUtils;

import feign.Request.Options;
import okhttp3.Headers;
import okhttp3.OkHttpClient;

/**
 * @author liuyulong
 * @since 2025-04-11
 */
public class MicloudApiUtils {

	public static final Options OPTIONS = new Options(Duration.ofSeconds(5), Duration.ofSeconds(5), true);

	public static final Map<String, String> HEADERS = MicloudApiUtils.getDefaultHeaders();

	public static final Map<String, String> COOKIES = MicloudApiUtils.getDefaultCookies();

	public static Map<String, String> getUserIdCookies(String userId) {
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.putAll(COOKIES);
		hashMap.put("userId", userId);
		return hashMap;
	}

	public static String getRandomAgentId() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder(13);
		for (int i = 0; i < 13; i++) {
			// 生成65-70(A-F)的ASCII码后转换为字符
			char randomChar = (char) (random.nextInt(6) + 'A');
			sb.append(randomChar);
		}
		return sb.toString();
	}

	public static String getRandomString(int length) {
		String letters = "abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		StringBuilder resultStr = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(letters.length());
			resultStr.append(letters.charAt(randomIndex));
		}
		return resultStr.toString();
	}

	private static Map<String, String> getDefaultHeaders() {
		String userAgent = "Android-7.1.1-1.0.0-ONEPLUS A3010-136-" + getRandomAgentId()
				+ " APP/xiaomi.smarthome APPV/62830";
		return Map.of("User-Agent", userAgent);
	}

	private static Map<String, String> getDefaultCookies() {
		return Map.of("sdkVersion", "3.8.6", "deviceId", getRandomString(6));
	}

	public static Headers getHeaders(Map<String, String> headers, Map<String, String> cookies) {
		String requestCookies = cookies.entrySet().stream()
				.map(entry -> StringUtils.join(entry.getKey(), "=", entry.getValue())).collect(Collectors.joining(";"));
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.putAll(headers);
		hashMap.put("Cookie", requestCookies);
		return Headers.of(hashMap);
	}

	public static String retriveLocationServiceToken(String location) {
		OkHttpClient okHttpClient = new OkHttpClient//
				.Builder()//
				.callTimeout(Duration.ofMillis(5000))//
				.connectTimeout(Duration.ofMillis(5000))//
				.readTimeout(Duration.ofMillis(5000))//
				.writeTimeout(Duration.ofMillis(5000))//
				.build();

		Headers headers = getHeaders(MicloudApiUtils.HEADERS, MicloudApiUtils.COOKIES);

		List<String> responseCookies = OkHttpClientUtils.get(okHttpClient, location, headers,
				(response, responseBody) -> {
					return response.headers("Set-Cookie");
				});

		String serviceTokenCookieKey = "serviceToken=";
		String serviceToken = null;
		if (null != responseCookies) {
			serviceToken = responseCookies//
					.stream()//
					.filter(cookie -> -1 != cookie.indexOf(serviceTokenCookieKey))//
					.findAny()//
					.map(cookie -> {
						String valuePart = cookie
								.substring(cookie.indexOf(serviceTokenCookieKey) + serviceTokenCookieKey.length());
						return valuePart.substring(0, valuePart.indexOf(";"));
					})//
					.orElse(null);
		}

		return serviceToken;
	}

	public static String getDeviceString(String country, GetDeviceListRequest request) {
		String url = getApiUrl(country) + "/home/device_list";

		HashMap<String, String> headers = new HashMap<>();
		headers.putAll(MicloudApiUtils.HEADERS);
		headers.put("Accept-Encoding", "identity");
		headers.put("x-xiaomi-protocal-flag-cli", "PROTOCAL-HTTP2");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("MIOT-ENCRYPT-ALGORITHM", "ENCRYPT-RC4");

		HashMap<String, String> cookies = new HashMap<>();
		cookies.putAll(MicloudApiUtils.COOKIES);
		cookies.put("userId", request.getUserId());
		cookies.put("yetAnotherServiceToken", request.getServiceToken());
		cookies.put("serviceToken", request.getServiceToken());
		cookies.put("locale", request.getLocale());
		cookies.put("timezone", request.getTimezone());
		cookies.put("is_daylight", request.getIsDaylight());
		cookies.put("dst_offset", request.getDstOffset());
		cookies.put("channel", request.getChannel());

		// body
		// {"getVirtualModel":true,"getHuamiDevices":1,"get_split_device":false,"support_smart_home":true}
		return url;
	}

	public static String getApiUrl(String country) {
		String countryDomainPrefix = null;
		if ("cn".equals(country)) {
			countryDomainPrefix = "";
		} else {
			countryDomainPrefix = country + ".";
		}
		return "https://" + countryDomainPrefix + "api.io.mi.com/app";
	}

}
