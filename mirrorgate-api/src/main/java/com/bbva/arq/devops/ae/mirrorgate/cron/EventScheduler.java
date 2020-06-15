/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.cron;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.cron.handler.EventHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.service.EventService;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EventScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(EventScheduler.class);

    private final EventService eventService;

    private Long schedulerTimestamp = 0L;

    private final ConnectionHandler handler;

    private final BeanFactory beanFactory;

    @Autowired
    public EventScheduler(
        final EventService eventService,
        final ConnectionHandler handler,
        final BeanFactory beanFactory
    ) {

        this.eventService = eventService;
        this.handler = handler;
        this.beanFactory = beanFactory;
    }


    @Scheduled(fixedDelayString = "${events.scheduler.delay.millis}")
    void checkEventUpdates() {

        LOG.debug("Processing events for timestamp {}", schedulerTimestamp);

        final Set<String> dashboardIds = handler.getDashboardsWithSession();

        if (dashboardIds != null) {
            LOG.debug("Active dashboards {}", dashboardIds.size());
        }

        //process events
        if (! Objects.requireNonNull(dashboardIds).isEmpty()) {
            //query DB for last events
            final List<Event> unprocessedEvents = eventService.getEventsSinceTimestamp(schedulerTimestamp);
            if (! unprocessedEvents.isEmpty()) {

                //Filter events
                unprocessedEvents.stream()
                    .collect(Collectors.groupingBy(Event::getEventType))
                    .forEach((key, value) -> beanFactory.getBean(key.getValue(), EventHandler.class)
                        .processEvents(value, dashboardIds));
                //save last event timestamp to local variable
                schedulerTimestamp = unprocessedEvents.get(unprocessedEvents.size() - 1).getTimestamp();
                LOG.debug("Modified timestamp: {}", schedulerTimestamp);
            }

        }
    }

    @PostConstruct
    public void initSchedulerTimestamp() {

        final Event lastEvent = eventService.getLastEvent();

        if (lastEvent != null) {
            schedulerTimestamp = lastEvent.getTimestamp();
        }

        LOG.info("Scheduler initial timestamp set to {}", schedulerTimestamp);
    }

}
