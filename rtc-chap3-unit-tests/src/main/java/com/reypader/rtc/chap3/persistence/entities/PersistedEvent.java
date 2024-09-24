package com.reypader.rtc.chap3.persistence.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.reypader.rtc.chap3.controllers.resources.Event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 *
 * @author rmpader
 */
@Entity
@Table(name="events")
public class PersistedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name="start")
    private OffsetDateTime eventStart;
    @Column(name="end")
    private OffsetDateTime eventEnd;
    @Column(name="name")
    private String eventName;
    @Version
    @Column(name="version_lock")
    private int versionNum;

    public UUID getId() {
        return id;
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

    public int getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(int versionNum) {
        this.versionNum = versionNum;
    }

    public static PersistedEvent fromResource(Event event){
        PersistedEvent pe = new PersistedEvent();
        pe.setEventStart(event.getEventStart());
        pe.setEventEnd(event.getEventEnd());
        pe.setEventName(event.getEventName());
        return pe;
    }

    public Event toResource(){
        Event pe = new Event();
        pe.setEventStart(this.getEventStart());
        pe.setEventEnd(this.getEventEnd());
        pe.setEventName(this.getEventName());
        pe.setId(this.getId());
        return pe;
    }

}
