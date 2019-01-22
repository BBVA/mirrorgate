package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.FeatureService;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FeatureEventHandlerTest {

    @MockBean
    private ConnectionHandler connectionHandler;

    @MockBean
    private DashboardService dashboardService;

    @MockBean
    private FeatureService featureService;

    @MockBean
    private ProcessEventsHelper eventsHelper;

    private FeatureEventHandler eventHandler;

    @Before
    public void init(){

        eventHandler = new FeatureEventHandler(connectionHandler, featureService, eventsHelper);
    }

    @Test
    public void testDeletedEvents(){

        when(dashboardService.getDashboardWithNames(anyList())).thenReturn(Collections.emptyList());

        eventHandler.processEvents(Collections.singletonList(new Event()), Collections.emptySet());

        verify(connectionHandler, times(1)).sendEventUpdateMessageToAll(EventType.FEATURE);
    }

}
