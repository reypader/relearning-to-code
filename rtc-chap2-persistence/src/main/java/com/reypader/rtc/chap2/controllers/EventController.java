package com.reypader.rtc.chap2.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.reypader.rtc.chap2.controllers.resources.Event;
import jakarta.validation.Valid;

/**
 *
 * @author rmpader
 */
@RestController
public class EventController {

    private Map<UUID, Event> eventRepository = new HashMap<>();

    @PostMapping("/events/")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        UUID id = UUID.randomUUID();
        event.setId(id);
        eventRepository.put(id, event);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable("id") UUID id) {
        if (eventRepository.containsKey(id)) {
            return ResponseEntity.ok(eventRepository.get(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/events/")
    public ResponseEntity<Collection<Event>> getEvents() {
        return ResponseEntity.ok(eventRepository.values());
    }
}
