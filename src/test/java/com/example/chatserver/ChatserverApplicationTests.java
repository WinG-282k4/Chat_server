package com.example.chatserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ChatserverApplicationTests {

	private static final Log log = LogFactory.getLog(ChatserverApplicationTests.class);

	@Test
	void contextLoads() {
		String password = "123456";
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		String encodedPassword = passwordEncoder.encode(password);
		log.info("Encode 1: " + encodedPassword);
		String encodedPassword2 = passwordEncoder.encode(password);
		log.info("Encode 2: " + encodedPassword2);
	}

}
