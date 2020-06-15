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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.dto.NotificationDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Notification;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NotificationServiceTestsIT {

    private static final NotificationDTO dto1 = new NotificationDTO();
    private static final NotificationDTO dto2 = new NotificationDTO();

    @Autowired
    private NotificationService service;

    @Autowired
    private EventService eventService;

    @BeforeClass
    public static void init() {
        dto1.setDashboardIds(Arrays.asList("dashboard1", "dashboard2"));
        dto1.setMessage("Notification message");

        dto2.setDashboardIds(Collections.singletonList("dashboard1"));
        dto2.setMessage("Another notification message");
    }


    @Test
    public void testSaveNotificationAndEvent() {

        service.saveNotification(dto1);

        final Event savedEvent = eventService.getLastEvent();
        final Notification notification = service.getNotificationForDashboard("dashboard1");

        assertSame(savedEvent.getEventType(), EventType.NOTIFICATION);
        assertEquals(0, notification.getMessage().compareTo("Notification message"));
        assertEquals(savedEvent.getCollectionId(), notification.getId());
    }

    @Test
    public void testNotificationsById() {

        final Notification result = service.saveNotification(dto1);
        final Notification result2 = service.saveNotification(dto2);

        final List<Notification> notificationsById = service
            .getNotificationsById(Arrays.asList(result.getId(), result2.getId()));
        final List<String> messages = notificationsById
            .stream()
            .map(Notification::getMessage)
            .collect(Collectors.toList());

        assertEquals(2, notificationsById.size());
        assertTrue(messages.containsAll(Arrays.asList("Notification message", "Another notification message")));
    }
}