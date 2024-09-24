package com.reypader.rtc.chap3.persistence.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.reypader.rtc.chap3.controllers.resources.Event;

public class PersistedEventTest {
    @Test
    public void testFromResource() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime later = now.plus(1, ChronoUnit.MINUTES);
        Event testEvent = new Event();
        testEvent.setEventStart(now);
        testEvent.setEventEnd(later);
        testEvent.setEventName("test_event");

        PersistedEvent actual = PersistedEvent.fromResource(testEvent);
        assertEquals(now, actual.getEventStart());
        assertEquals(later, actual.getEventEnd());
        assertEquals("test_event", actual.getEventName());
    }

    @Test
    public void testToResource() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime later = now.plus(1, ChronoUnit.MINUTES);
        PersistedEvent testEvent = new PersistedEvent();
        testEvent.setId(id);
        testEvent.setEventStart(now);
        testEvent.setEventEnd(later);
        testEvent.setEventName("test_event");

        Event actual = testEvent.toResource();
        assertEquals(now, actual.getEventStart());
        assertEquals(later, actual.getEventEnd());
        assertEquals("test_event", actual.getEventName());
        assertEquals(id, actual.getId());
    }
}
