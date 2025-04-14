/**
 * 
 */
package com.apoollo.auto.connect.micloud.api;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import feign.Request.Options;

/**
 * @author liuyulong
 * @since 2025-04-11
 */
public class MicloudApiUtils {

	public static final Options OPTIONS = new Options(Duration.ofSeconds(5), Duration.ofSeconds(5), false);

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
}
