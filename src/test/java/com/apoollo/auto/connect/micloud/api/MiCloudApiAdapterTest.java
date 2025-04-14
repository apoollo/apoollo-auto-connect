package com.apoollo.auto.connect.micloud.api;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.alibaba.fastjson2.JSON;
import com.apoollo.auto.connect.micloud.api.response.ServiceLoginAuth2Response;

@SpringBootTest
@ActiveProfiles("local-dev")
@AutoConfigureMockMvc
public class MiCloudApiAdapterTest {

	@Autowired
	private MiCloudApiAdapter miCloudApiAdapter;

	@Test
	public void serviceLoginSign() throws IOException {

		String username = "13126907778";
		String password = "1qaz2wsx";
		String sign = miCloudApiAdapter.serviceLoginSign(username);
		ServiceLoginAuth2Response serviceLoginAuth2Response = miCloudApiAdapter.serviceLoginAuth2(username, password,
				sign);

		System.out.println();

	}
}