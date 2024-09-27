package com.reypader.rtc.chap4.persistence.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.reypader.rtc.chap4.controllers.resources.Event;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

/**
 *
 * @author rmpader
 */
@DynamoDbBean
public class PersistedEvent {
    
    private UUID id;
    private String eventName;
    private OffsetDateTime eventStart;
    private OffsetDateTime eventEnd;

    /*
     * Note: Only for demonstration's sake. 
     * 
     * For better guidance on choosing Partition and Sort Keys, see this page 
     * https://aws.amazon.com/blogs/database/choosing-the-right-dynamodb-partition-key/
     */
    @DynamoDbPartitionKey 
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

    public PersistedEvent() {
        this.id = UUID.randomUUID();
    }

    public static PersistedEvent fromResource(Event event) {
        PersistedEvent pe = new PersistedEvent();
        pe.setEventStart(event.getEventStart());
        pe.setEventEnd(event.getEventEnd());
        pe.setEventName(event.getEventName());
        return pe;
    }

    public Event toResource() {
        Event pe = new Event();
        pe.setEventStart(this.getEventStart());
        pe.setEventEnd(this.getEventEnd());
        pe.setEventName(this.getEventName());
        pe.setId(this.getId());
        return pe;
    }

    // required by Dynamo
    public void setId(UUID id2) {
        this.id = id2;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((eventName == null) ? 0 : eventName.hashCode());
        result = prime * result + ((eventStart == null) ? 0 : eventStart.hashCode());
        result = prime * result + ((eventEnd == null) ? 0 : eventEnd.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PersistedEvent other = (PersistedEvent) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (eventName == null) {
            if (other.eventName != null)
                return false;
        } else if (!eventName.equals(other.eventName))
            return false;
        if (eventStart == null) {
            if (other.eventStart != null)
                return false;
        } else if (!eventStart.equals(other.eventStart))
            return false;
        if (eventEnd == null) {
            if (other.eventEnd != null)
                return false;
        } else if (!eventEnd.equals(other.eventEnd))
            return false;
        return true;
    }

    
    

}
