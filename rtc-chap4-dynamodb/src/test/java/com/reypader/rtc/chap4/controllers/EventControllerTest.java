package com.reypader.rtc.chap4.controllers;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;


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

import com.reypader.rtc.chap4.controllers.resources.Event;
import com.reypader.rtc.chap4.persistence.entities.PersistedEvent;
import com.reypader.rtc.chap4.persistence.repositories.PersistedEventRepository;

/**
 *
 * @author rmpader
 */
public class EventControllerTest {

    private final PersistedEventRepository mockRepo = mock(PersistedEventRepository.class);
    private final EventController underTest = new EventController(mockRepo);

    @Test
    public void testCreateEvent() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime later = now.plus(1, ChronoUnit.MINUTES);
        Event testEvent = new Event();
        testEvent.setEventStart(now);
        testEvent.setEventEnd(later);
        testEvent.setEventName("test_event");

        when(mockRepo.save(any(PersistedEvent.class))).thenAnswer((InvocationOnMock invocation) -> {
            // for the sake of testing let's see if this new instance will be returned.
            PersistedEvent pe = new PersistedEvent();
            pe.setEventName("modified");
            pe.setEventStart(later);
            pe.setEventEnd(now);
            return pe;
        });

        ResponseEntity<Event> actual = underTest.createEvent(testEvent);

        ArgumentCaptor<PersistedEvent> captor = ArgumentCaptor.forClass(PersistedEvent.class);
        verify(mockRepo, times(1)).save(captor.capture());
        assertEquals(captor.getValue(), PersistedEvent.fromResource(testEvent)); // am I being lazy or wise here?
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        Event body = actual.getBody();
        assertNotNull(body);
        assertEquals(later, body.getEventStart());
        assertEquals(now, body.getEventEnd());
        assertEquals("modified", body.getEventName());
        // no setter for ID, we can't assert that but we're testing that in PersistedEventTest anyway
    }

    @Test
    public void testGetEvent() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime later = now.plus(1, ChronoUnit.MINUTES);
        when(mockRepo.findById(id)).thenAnswer((InvocationOnMock invocation) -> {
            PersistedEvent pe = new PersistedEvent();
            pe.setEventName("found");
            pe.setEventStart(later);
            pe.setEventEnd(now);
            return Optional.of(pe);
        });

        ResponseEntity<Event> actual = underTest.getEvent(id);

        verify(mockRepo, times(1)).findById(id);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Event body = actual.getBody();
        assertNotNull(body);
        assertEquals(later, body.getEventStart());
        assertEquals(now, body.getEventEnd());
        assertEquals("found", body.getEventName());
    }

    @Test
    public void testGetEvents() {
        when(mockRepo.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            PersistedEvent a = new PersistedEvent();
            a.setEventName("element1");

            PersistedEvent b = new PersistedEvent();
            b.setEventName("element2");
            return Arrays.asList(a, b);
        });

        ResponseEntity<Collection<Event>> actual = underTest.getEvents();

        verify(mockRepo, times(1)).findAll();
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Collection<Event> body = actual.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
        assertTrue(body.stream().anyMatch(e -> "element1".equals(e.getEventName())));
        assertTrue(body.stream().anyMatch(e -> "element2".equals(e.getEventName())));
    }

}