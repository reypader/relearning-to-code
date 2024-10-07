package com.reypader.rtc.chap5.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.reypader.rtc.chap5.controllers.resources.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import com.reypader.rtc.chap5.persistence.entities.PersistedEvent;
import com.reypader.rtc.chap5.persistence.repositories.PersistedEventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


class EventControllerWebTest {


    private PersistedEventRepository mockRepo = mock(PersistedEventRepository.class);
    private EventController controller;
    private WebTestClient client;

    @BeforeEach
    public void beforeEach() {
        controller = new EventController(mockRepo);
        client = WebTestClient.bindToController(controller).build();
    }

    @Test
    public void testGetRequest() throws Exception {

        PersistedEvent mockPE = new PersistedEvent();
        UUID id = mockPE.getId();
        mockPE.setEventName("X");
        when(mockRepo.findById(id)).thenReturn(Mono.just(mockPE));

        client.get().uri("/events/" + id).exchange()
                .expectStatus().isOk()
                .expectBody(Event.class)
                .consumeWith(result -> {
                    Event responseBody = result.getResponseBody();
                    assertNotNull(responseBody);
                    assertNull(responseBody.getEventStart());
                    assertNull(responseBody.getEventEnd());
                    assertEquals("X", responseBody.getEventName());
                    assertEquals(id, responseBody.getId());
                });
    }

    @Test
    public void testGetAllRequest() throws Exception {
        PersistedEvent mockPE = new PersistedEvent();
        UUID id = mockPE.getId();
        mockPE.setEventName("X");
        when(mockRepo.findAll()).thenReturn(Flux.just(mockPE));

        client.get().uri("/events/").exchange()
                .expectStatus().isOk()
                .expectBodyList(Event.class)
                .hasSize(1)
                .consumeWith(result -> {
                    List<Event> responseBody = result.getResponseBody();
                    assertNotNull(responseBody);
                    Event event = responseBody.getFirst();
                    assertNull(event.getEventStart());
                    assertNull(event.getEventEnd());
                    assertEquals("X", event.getEventName());
                    assertEquals(id, event.getId());
                });

    }

    @Test
    public void testSaveRequest() throws Exception {
        PersistedEvent mockPE = new PersistedEvent();
        UUID id = mockPE.getId();
        mockPE.setEventName("X");
        when(mockRepo.save(any(PersistedEvent.class))).thenReturn(Mono.just(mockPE));

        Event testBody = new Event();
        testBody.setEventName("Z");
        client.post().uri("/events/")
                .body(Mono.just(testBody), Event.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Event.class)
                .consumeWith(result -> {
                    Event event = result.getResponseBody();
                    assertNotNull(event);
                    assertNull(event.getEventStart());
                    assertNull(event.getEventEnd());
                    assertEquals("X", event.getEventName());
                    assertEquals(id, event.getId());
                });
    }

    @Test
    public void testSaveRequestValidation() throws Exception {
        Event testBody = new Event();
        testBody.setEventName("I_HAD_DINNER_BUT_I_HAD_NO_FORK_SO_I_USED_MY_HANDS_INSTEAD");
        client.post().uri("/events/")
                .body(Mono.just(testBody), Event.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

}
