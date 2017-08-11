package com.bbva.arq.devops.ae.mirrorgate.cron;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.EventService;
import com.bbva.arq.devops.ae.mirrorgate.support.TestUtil;
import com.bbva.arq.devops.ae.mirrorgate.websocket.SocketHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.rule.OutputCapture;

@RunWith(MockitoJUnitRunner.class)
public class EventSchedulerTest {

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Mock
    private EventService eventService;

    @Mock
    private BuildService buildService;

    @Mock
    private SocketHandler socketHandler;

    @Mock
    private DashboardService dashboardService;


    private EventScheduler eventScheduler;


    @Before
    public void init(){

        TestUtil.setLoggingLevel(Level.DEBUG);
        eventScheduler = new EventScheduler(eventService, buildService, socketHandler, dashboardService);
    }

    @Test
    public void testSchedulerTimestampIsModified() throws IOException {

        when(eventService.getEventsSinceTimestamp(anyLong())).thenReturn(Arrays.asList(createBuildEvent()));
        when(eventService.getLastEvent()).thenReturn(null);

        eventScheduler.checkEventUpdates();

        assertTrue(outputCapture.toString().contains("1234567"));

    }

    private Event createBuildEvent(){

        Event buildEvent = new Event();

        buildEvent.setTimestamp(1234567L);
        buildEvent.setEventType(EventType.BUILD);

        return buildEvent;
    }

}
