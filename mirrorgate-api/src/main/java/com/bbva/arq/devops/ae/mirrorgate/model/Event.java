package com.bbva.arq.devops.ae.mirrorgate.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
public class Event extends BaseIdModel {

    private Long timestamp;
    private Object eventTypeCollectionId;
    private EventType eventType;

    public Long getTimestamp() {
        return timestamp;
    }

    public Event setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Object getEventTypeCollectionId() {
        return eventTypeCollectionId;
    }

    public Event setEventTypeCollectionId(Object eventTypeCollectionId) {
        this.eventTypeCollectionId = eventTypeCollectionId;
        return this;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Event setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

}
