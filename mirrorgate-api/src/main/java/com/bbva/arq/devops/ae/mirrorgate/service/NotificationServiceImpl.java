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

import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Notification;
import com.bbva.arq.devops.ae.mirrorgate.repository.NotificationRepository;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final EventService eventService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository repository, EventService eventService){

        this.repository = repository;
        this.eventService = eventService;
    }

    @Override
    public List<Notification> getNotificationsById(List<ObjectId> notificationIds) {

        return repository.findAllByIdIn(notificationIds);
    }

    @Override
    public Notification getNotificationForDashboard(String dashboardId){

        return repository.findByDashboardsToNotifyOrderByTimestampDesc(dashboardId);
    }

    @Override
    public Notification saveNotification(List<String> dashboardIds, String message){

        Notification notification = new Notification();
        notification.setDashboardsToNotify(dashboardIds);
        notification.setMessage(message);
        notification.setTimestamp(System.currentTimeMillis());

        Notification result = repository.save(notification);

        eventService.saveEvent(notification, EventType.NOTIFICATION);

        return result;
    }
}
