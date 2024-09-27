package com.reypader.rtc.chap4.controllers;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.reypader.rtc.chap4.controllers.resources.Event;
import com.reypader.rtc.chap4.persistence.entities.PersistedEvent;

import jakarta.validation.Valid;

import com.reypader.rtc.chap4.persistence.repositories.PersistedEventRepository;

/**
 *
 * @author rmpader
 */
@RestController
public class EventController {
    private final PersistedEventRepository eventRepository;

    public EventController(PersistedEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostMapping("/events/")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        PersistedEvent persistedEvent = eventRepository.save(PersistedEvent.fromResource(event));
        return ResponseEntity.status(HttpStatus.CREATED).body(persistedEvent.toResource());
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable("id") UUID id) {
        return eventRepository.findById(id).map(pe-> ResponseEntity.ok(pe.toResource())).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/events/")
    public ResponseEntity<Collection<Event>> getEvents() {
        return ResponseEntity.ok(eventRepository.findAll().stream().map(PersistedEvent::toResource).collect(Collectors.toSet()));
    }
}
