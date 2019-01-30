package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.EventNotificationDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventNotification;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
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

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EventNotificationServiceTestsIT {

    private static final EventNotificationDTO dto1 = new EventNotificationDTO();
    private static final EventNotificationDTO dto2 = new EventNotificationDTO();

    @Autowired
    private EventNotificationService service;

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

        service.saveEventNotification(dto1);

        Event savedEvent = eventService.getLastEvent();
        EventNotification notification = service.getEventNotificationForDashboard("dashboard1");

        assertSame(savedEvent.getEventType(), EventType.NOTIFICATION);
        assertEquals(0, notification.getMessage().compareTo("Notification message"));
        assertEquals(savedEvent.getEventTypeCollectionId(), notification.getId());
    }

    @Test
    public void testNotificationsById(){

        EventNotification result = service.saveEventNotification(dto1);
        EventNotification result2 = service.saveEventNotification(dto2);

        List<EventNotification> notificationsById =
            service.getEventNotificationsById(Arrays.asList(result.getId(), result2.getId()));
        List<String> messages =
            notificationsById.stream().map(EventNotification::getMessage).collect(Collectors.toList());

        assertEquals(2, notificationsById.size());
        assertTrue(messages.containsAll(Arrays.asList("Notification message", "Another notification message")));
    }
}
