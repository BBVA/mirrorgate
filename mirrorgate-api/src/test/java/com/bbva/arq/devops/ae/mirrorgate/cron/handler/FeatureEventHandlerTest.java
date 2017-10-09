package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.FeatureService;
import java.util.ArrayList;
import java.util.Arrays;
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

    private FeatureEventHandler eventHandler;

    @Before
    public void init(){

        eventHandler = new FeatureEventHandler(connectionHandler, dashboardService, featureService);
    }

    @Test
    public void testDeletedEvents(){

        when(dashboardService.getDashboardWithNames(any(ArrayList.class))).thenReturn(new ArrayList());

        eventHandler.processEvents(Arrays.asList(new Event()), Collections.emptySet());

        verify(connectionHandler, times(1)).sendEventUpdateMessageToAll(EventType.FEATURE);
    }

}
