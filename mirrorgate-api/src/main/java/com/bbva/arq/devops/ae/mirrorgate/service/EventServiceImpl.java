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

package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.BaseModel;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.repository.EventRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;


    @Autowired
    public EventServiceImpl(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void saveEvent(final BaseModel model, final EventType type) {
        LOG.info("Saving event with Id: {}", model.getId());

        eventRepository.save(new Event()
            .setEventType(type)
            .setCollectionId(model.getId())
            .setTimestamp(System.currentTimeMillis())
        );
    }

    @Override
    public void saveEvents(final Iterable<? extends BaseModel> models, final EventType type) {
        LOG.info("Saving list of events");

        final List<Event> eventList = StreamSupport.stream(models.spliterator(), false)
            .map(model -> new Event()
                .setEventType(type)
                .setCollectionId(model.getId())
                .setTimestamp(System.currentTimeMillis())
            ).collect(Collectors.toList());

        eventRepository.saveAll(eventList);
    }

    @Override
    public Event getLastEvent() {
        return eventRepository.findFirstByOrderByTimestampDesc();
    }

    @Override
    public List<Event> getEventsSinceTimestamp(final Long timestamp) {
        return eventRepository.findByTimestampGreaterThanOrderByTimestampAsc(timestamp);
    }
}
