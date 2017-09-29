package com.bbva.arq.devops.ae.mirrorgate.cron;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ServerSideEventsHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.EventService;
import com.bbva.arq.devops.ae.mirrorgate.support.TestUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventSchedulerTest {

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @MockBean
    private EventService eventService;

    @MockBean
    private BuildService buildService;

    @MockBean
    private ServerSideEventsHandler eventsHandler;

    @MockBean
    private DashboardService dashboardService;

    @Autowired
    private EventScheduler eventScheduler;


    @Before
    public void init(){

        TestUtil.setLoggingLevel(Level.DEBUG);
    }

    @Test
    public void testSchedulerTimestampIsModified() throws IOException {

        when(eventService.getEventsSinceTimestamp(anyLong())).thenReturn(Arrays.asList(createBuildEvent(),createFeatureEvent()));
        when(eventService.getLastEvent()).thenReturn(null);
        when(eventsHandler.getDashboardsWithSession()).thenReturn(new HashSet<>(Arrays.asList("123")));

        eventScheduler.checkEventUpdates();

        assertTrue(outputCapture.toString().contains("1234567"));

    }

    private Event createBuildEvent(){

        Event buildEvent = new Event();

        buildEvent.setTimestamp(1234567L);
        buildEvent.setEventType(EventType.BUILD);

        return buildEvent;
    }

    private Event createFeatureEvent(){

        Event buildEvent = new Event();

        buildEvent.setTimestamp(1234567L);
        buildEvent.setEventType(EventType.FEATURE);

        return buildEvent;
    }

}
