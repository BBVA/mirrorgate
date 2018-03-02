package com.bbva.arq.devops.ae.mirrorgate.service;

import static org.junit.Assert.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventNotification;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EventNotificationServiceTestsIT {

    @Autowired
    private EventNotificationService service;

    @Autowired
    private EventService eventService;

    @Test
    public void testSaveNotificationAndEvent(){

        EventNotification result = service.saveEventNotification(Arrays.asList("dashboard1", "dashboard2"), "Notification message");

        Event savedEvent = eventService.getLastEvent();
        EventNotification notification = service.getEventNotificationForDashboard("dashboard1");

        assertTrue(savedEvent.getEventType() == EventType.NOTIFICATION);
        assertTrue(notification.getMessage().compareTo("Notification message") == 0);
        assertTrue(savedEvent.getEventTypeCollectionId().equals(notification.getId()));
    }

    @Test
    public void testNotificationsById(){

        EventNotification result = service.saveEventNotification(Arrays.asList("dashboard1", "dashboard2"), "Notification message");
        EventNotification result2 = service.saveEventNotification(Arrays.asList("dashboard1"), "Another notification message");

        List<EventNotification> notificationsById = service.getEventNotificationsById(Arrays.asList(result.getId(), result2.getId()));
        List<String> messages = notificationsById.stream().map(n -> n.getMessage()).collect(Collectors.toList());

        assertTrue(notificationsById.size() == 2);
        assertTrue(messages.containsAll(Arrays.asList("Notification message", "Another notification message")));
    }
}
