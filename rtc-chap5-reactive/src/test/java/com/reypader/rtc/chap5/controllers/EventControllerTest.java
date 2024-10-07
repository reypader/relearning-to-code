package com.reypader.rtc.chap5.controllers;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.invocation.InvocationOnMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.reypader.rtc.chap5.controllers.resources.Event;
import com.reypader.rtc.chap5.persistence.entities.PersistedEvent;
import com.reypader.rtc.chap5.persistence.repositories.PersistedEventRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.publisher.TestPublisher;

/**
 * @author rmpader
 */
public class EventControllerTest {

    private final PersistedEventRepository mockRepo = mock(PersistedEventRepository.class);
    private final EventController underTest = new EventController(mockRepo);

    @Test
    public void testCreateEvent() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime later = now.plusMinutes(1);
        Event testEvent = new Event();
        testEvent.setEventStart(now);
        testEvent.setEventEnd(later);
        testEvent.setEventName("test_event");

        TestPublisher<PersistedEvent> tp = TestPublisher.create();
        when(mockRepo.save(any(PersistedEvent.class))).thenAnswer((InvocationOnMock invocation) -> tp.mono());

        StepVerifier.create(underTest.createEvent(testEvent)).then(() -> {
            // for the sake of testing let's see if this new instance will be returned.
            PersistedEvent pe = new PersistedEvent();
            pe.setEventName("modified");
            pe.setEventStart(later);
            pe.setEventEnd(now);
            tp.emit(pe);
        }).assertNext(actual -> {
            assertEquals(HttpStatus.CREATED, actual.getStatusCode());
            Event body = actual.getBody();
            assertNotNull(body);
            assertEquals(later, body.getEventStart());
            assertEquals(now, body.getEventEnd());
            assertEquals("modified", body.getEventName());
            // no setter for ID, we can't assert that but we're testing that in PersistedEventTest anyway
        }).verifyComplete();

        ArgumentCaptor<PersistedEvent> captor = ArgumentCaptor.forClass(PersistedEvent.class);
        verify(mockRepo, times(1)).save(captor.capture());
        PersistedEvent captured = captor.getValue();
        assertEquals(captured.getEventName(), "test_event");
        assertEquals(captured.getEventStart(), now);
        assertEquals(captured.getEventEnd(), later);
    }

    @Test
    public void testGetEvent() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime later = now.plusMinutes(1);

        TestPublisher<PersistedEvent> tp = TestPublisher.create();
        when(mockRepo.findById(id)).thenAnswer((InvocationOnMock invocation) -> tp.mono());


        StepVerifier.create(underTest.getEvent(id))
                .then(() -> {
                    PersistedEvent pe = new PersistedEvent();
                    pe.setEventName("found");
                    pe.setEventStart(later);
                    pe.setEventEnd(now);
                    tp.emit(pe);
                }).assertNext(actual -> {
                    assertEquals(HttpStatus.OK, actual.getStatusCode());
                    Event body = actual.getBody();
                    assertNotNull(body);
                    assertEquals(later, body.getEventStart());
                    assertEquals(now, body.getEventEnd());
                    assertEquals("found", body.getEventName());
                }).verifyComplete();

        verify(mockRepo, times(1)).findById(id);
    }

    @Test
    public void testGetEvents() {
        TestPublisher<PersistedEvent> tp = TestPublisher.create();
        when(mockRepo.findAll()).thenAnswer((InvocationOnMock invocation) -> tp.flux().log());

        ResponseEntity<Flux<Event>> actual = underTest.getEvents();

        verify(mockRepo, times(1)).findAll();
        assertEquals(HttpStatus.OK, actual.getStatusCode());

        StepVerifier.create(Objects.requireNonNull(actual.getBody()))
                .then(()->{
                    PersistedEvent a = new PersistedEvent();
                    a.setEventName("element1");

                    PersistedEvent b = new PersistedEvent();
                    b.setEventName("element2");
                    tp.emit(a, b);
                })
                .assertNext(e -> {
                    assertEquals("element1", e.getEventName());
                }).assertNext(e -> {
                    assertEquals("element2", e.getEventName());
                })
                .verifyComplete();
    }

}