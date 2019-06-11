package com.bbva.arq.devops.ae.mirrorgate.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
public class Event extends BaseIdModel {

    @Indexed
    private Long timestamp;
    private Object collectionId;
    private EventType eventType;

    public Long getTimestamp() {
        return timestamp;
    }

    public Event setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Object getCollectionId() {
        return collectionId;
    }

    public Event setCollectionId(Object collectionId) {
        this.collectionId = collectionId;
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
