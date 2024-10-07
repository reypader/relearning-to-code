package com.reypader.rtc.chap4;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.reypader.rtc.chap4.controllers.EventController;

@SpringBootTest
@ActiveProfiles("test")
public class RtcChap4DynamoApplicationTests {

	@Autowired
	private EventController eventController;

	@Test
	void contextLoads() {
		assertNotNull(eventController);
	}

}
