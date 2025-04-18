package com.apoollo.auto.connect.micloud.api;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.apoollo.auto.connect.brand.xiaomi.cloud.api.MiCloudApiAdapter;
import com.apoollo.auto.connect.brand.xiaomi.cloud.api.response.ServiceLoginAuth2Response;

@SpringBootTest
@ActiveProfiles("local-dev")
@AutoConfigureMockMvc
public class MiCloudApiAdapterTest {

	@Autowired
	private MiCloudApiAdapter miCloudApiAdapter;

	@Test
	public void serviceLoginSign() throws IOException {

		String username = "xxxx";
		String password = "xxx";

		String sign = miCloudApiAdapter.serviceLoginSign(username);

		String location = null;
		if (sign.startsWith("http")) {
			location = sign;
		} else {
			ServiceLoginAuth2Response serviceLoginAuth2Response = miCloudApiAdapter.serviceLoginAuth2(username,
					password, sign);
			location = serviceLoginAuth2Response.getLocation();
		}

		System.out.println("location:" + location);

	}
}