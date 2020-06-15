/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    public Event setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Object getCollectionId() {
        return collectionId;
    }

    public Event setCollectionId(final Object collectionId) {
        this.collectionId = collectionId;
        return this;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Event setEventType(final EventType eventType) {
        this.eventType = eventType;
        return this;
    }

}
