package com.bbva.arq.devops.ae.mirrorgate.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
public class Event extends BaseModel{

    @Indexed
    private Long timestamp;
    private ObjectId eventTypeCollectionId;
    private EventType eventType;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public ObjectId getEventTypeCollectionId() {
        return eventTypeCollectionId;
    }

    public void setEventTypeCollectionId(ObjectId eventTypeCollectionId) {
        this.eventTypeCollectionId = eventTypeCollectionId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
