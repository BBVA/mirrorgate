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
package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.EventNotificationDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.EventNotification;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.repository.EventNotificationRepository;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventNotificationServiceImpl implements EventNotificationService {

    private final EventNotificationRepository repository;
    private final EventService eventService;

    @Autowired
    public EventNotificationServiceImpl(EventNotificationRepository repository, EventService eventService){

        this.repository = repository;
        this.eventService = eventService;
    }

    @Override
    public List<EventNotification> getEventNotificationsById(List<ObjectId> notificationIds) {

        return repository.findAllByIdIn(notificationIds);
    }

    @Override
    public EventNotification getEventNotificationForDashboard(String dashboardId){

        return repository.findByDashboardsToNotifyOrderByTimestampDesc(dashboardId);
    }

    @Override
    public EventNotification saveEventNotification(EventNotificationDTO eventNotificationDTO){

        EventNotification notification = new EventNotification();
        notification.setDashboardsToNotify(eventNotificationDTO.getDashboardIds());
        notification.setMessage(eventNotificationDTO.getMessage());
        notification.setTimestamp(System.currentTimeMillis());

        EventNotification result = repository.save(notification);

        eventService.saveEvent(notification, EventType.NOTIFICATION);

        return result;
    }
}
