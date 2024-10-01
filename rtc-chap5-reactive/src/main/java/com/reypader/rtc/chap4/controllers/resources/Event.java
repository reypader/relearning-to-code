package com.reypader.rtc.chap5.controllers.resources;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Size;

/**
 *
 * @author rmpader
 */
public class Event {
    private UUID id;
    @JsonProperty("start")
    private OffsetDateTime eventStart;
    @JsonProperty("end")
    private OffsetDateTime eventEnd;
    @JsonProperty("name")
    @Size(max=50)
    private String eventName;
    
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public OffsetDateTime getEventStart() {
        return eventStart;
    }
    public void setEventStart(OffsetDateTime eventStart) {
        this.eventStart = eventStart;
    }
    public OffsetDateTime getEventEnd() {
        return eventEnd;
    }
    public void setEventEnd(OffsetDateTime eventEnd) {
        this.eventEnd = eventEnd;
    }
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    
}
