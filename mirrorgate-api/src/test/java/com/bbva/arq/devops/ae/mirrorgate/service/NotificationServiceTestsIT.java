package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.NotificationDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Notification;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
    public static void init(){
        dto1.setDashboardIds(Arrays.asList("dashboard1", "dashboard2"));
        dto1.setMessage("Notification message");

        dto2.setDashboardIds(Collections.singletonList("dashboard1"));
        dto2.setMessage("Another notification message");
    }


    @Test
    public void testSaveNotificationAndEvent(){

        service.saveNotification(dto1);

        Event savedEvent = eventService.getLastEvent();
        Notification notification = service.getNotificationForDashboard("dashboard1");

        assertSame(savedEvent.getEventType(), EventType.NOTIFICATION);
        assertEquals(0, notification.getMessage().compareTo("Notification message"));
        assertEquals(savedEvent.getCollectionId(), notification.getId());
    }

    @Test
    public void testNotificationsById(){

        Notification result = service.saveNotification(dto1);
        Notification result2 = service.saveNotification(dto2);

        List<Notification> notificationsById =
            service.getNotificationsById(Arrays.asList(result.getId(), result2.getId()));
        List<String> messages =
            notificationsById.stream().map(Notification::getMessage).collect(Collectors.toList());

        assertEquals(2, notificationsById.size());
        assertTrue(messages.containsAll(Arrays.asList("Notification message", "Another notification message")));
    }
}
