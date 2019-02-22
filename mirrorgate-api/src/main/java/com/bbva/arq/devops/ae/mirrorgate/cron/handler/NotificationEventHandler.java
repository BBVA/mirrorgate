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
package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventNotification;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.EventNotificationService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component(value = "NotificationType")
public class NotificationEventHandler implements EventHandler {

    private final ProcessEventsHelper eventsHelper;
    private final EventNotificationService eventNotificationService;

    @Autowired
    public NotificationEventHandler(ProcessEventsHelper eventsHelper,
        EventNotificationService eventNotificationService){

        this.eventsHelper = eventsHelper;
        this.eventNotificationService = eventNotificationService;
    }


    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {

        List<ObjectId> idList = eventList.stream()
            .map(Event::getEventTypeCollectionId)
            .filter(ObjectId.class::isInstance)
            .map(ObjectId.class::cast)
            .collect(Collectors.toList());

        List<EventNotification> listOfNotifications = eventNotificationService.getEventNotificationsById(idList);

        List<String> listOfDashboardsToNotify = listOfNotifications
            .stream()
            .flatMap(n -> n.getDashboardsToNotify() == null ? Stream.empty() : n.getDashboardsToNotify().stream())
            .collect(Collectors.toList());

        Predicate<Dashboard> dashboardFilter = d -> listOfDashboardsToNotify.contains(d.getName());
        eventsHelper.processEvents(dashboardIds, dashboardFilter, EventType.NOTIFICATION);
    }
}
