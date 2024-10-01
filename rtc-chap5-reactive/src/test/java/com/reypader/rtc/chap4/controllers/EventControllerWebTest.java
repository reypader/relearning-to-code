package com.reypader.rtc.chap5.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.reypader.rtc.chap5.persistence.entities.PersistedEvent;
import com.reypader.rtc.chap5.persistence.repositories.PersistedEventRepository;

@WebMvcTest(EventController.class)
class EventControllerWebTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersistedEventRepository mockRepo;

	@Test
	public void testGetRequest() throws Exception {
		PersistedEvent mockPE = new PersistedEvent();
		UUID id = mockPE.getId();
		mockPE.setEventName("X");
		when(mockRepo.findById(id)).thenReturn(Optional.of(mockPE));
		this.mockMvc.perform(get("/events/" + id)).andExpect(status().isOk())
				.andExpect(content().json("{'start':null,'end':null,'name':'X','id':'"+id+"'}"));
	}

	@Test
	public void testGetAllRequest() throws Exception {
		PersistedEvent mockPE = new PersistedEvent();
		UUID id = mockPE.getId();
		mockPE.setEventName("X");
		when(mockRepo.findAll()).thenReturn(Arrays.asList(mockPE));
		this.mockMvc.perform(get("/events/")).andExpect(status().isOk())
				.andExpect(content().json("[{'start':null,'end':null,'name':'X','id':'"+id+"'}]"));
	}

	@Test
	public void testSaveRequest() throws Exception {
		PersistedEvent mockPE = new PersistedEvent();
		UUID id = mockPE.getId();
		mockPE.setEventName("X");
		when(mockRepo.save(any(PersistedEvent.class))).thenReturn(mockPE);
		this.mockMvc.perform(post("/events/")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"start\":null,\"end\":null,\"name\":\"Z\"}"))
				.andExpect(status().isCreated())
				.andExpect(content().json("{'start':null,'end':null,'name':'X','id':'"+id+"'}"));
	}

	@Test
	public void testSaveRequestValidation() throws Exception {
		PersistedEvent mockPE = new PersistedEvent();
		mockPE.setEventName("X");
		when(mockRepo.save(any(PersistedEvent.class))).thenReturn(mockPE);
		this.mockMvc.perform(post("/events/")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"start\":null,\"end\":null,\"name\":\"I_HAD_DINNER_BUT_I_HAD_NO_FORK_SO_I_USED_MY_HANDS_INSTEAD\"}"))
				.andExpect(status().isBadRequest());
	}

}
