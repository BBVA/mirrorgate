/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.cron;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ServerSentEventsHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.EventService;
import com.bbva.arq.devops.ae.mirrorgate.support.TestUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventSchedulerTest {

    @Rule
    public final OutputCaptureRule outputCapture = new OutputCaptureRule();

    @MockBean
    private EventService eventService;

    @MockBean
    private BuildService buildService;

    @MockBean
    private ServerSentEventsHandler eventsHandler;

    @MockBean
    private DashboardService dashboardService;

    @Autowired
    private EventScheduler eventScheduler;


    @Before
    public void init() {
        TestUtil.setLoggingLevel(Level.DEBUG);
    }

    @Test
    public void testSchedulerTimestampIsModified() {
        when(eventService.getEventsSinceTimestamp(anyLong()))
            .thenReturn(Arrays.asList(createBuildEvent(), createIssueEvent()));
        when(eventService.getLastEvent()).thenReturn(null);
        when(eventsHandler.getDashboardsWithSession()).thenReturn(new HashSet<>(Collections.singletonList("123")));

        eventScheduler.checkEventUpdates();

        assertTrue(outputCapture.toString().contains("1234567"));

    }

    private Event createBuildEvent() {

        final Event buildEvent = new Event();

        buildEvent.setTimestamp(1234567L);
        buildEvent.setEventType(EventType.BUILD);

        return buildEvent;
    }

    private Event createIssueEvent() {

        final Event buildEvent = new Event();

        buildEvent.setTimestamp(1234567L);
        buildEvent.setEventType(EventType.ISSUE);

        return buildEvent;
    }
}