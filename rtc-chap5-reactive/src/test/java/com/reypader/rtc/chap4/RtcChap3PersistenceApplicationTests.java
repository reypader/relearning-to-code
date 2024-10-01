package com.reypader.rtc.chap5;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.reypader.rtc.chap5.controllers.EventController;

@SpringBootTest
@ActiveProfiles("test")
class RtcChap5PersistenceApplicationTests {

	@Autowired
	private EventController eventController;

	@Test
	void contextLoads() {
		assertNotNull(eventController);
	}

}
