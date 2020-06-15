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

package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.IssueService;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class IssueEventHandlerTest {

    @MockBean
    private ConnectionHandler connectionHandler;

    @MockBean
    private DashboardService dashboardService;

    @MockBean
    private IssueService issueService;

    @MockBean
    private ProcessEventsHelper eventsHelper;

    private IssueEventHandler eventHandler;

    @Before
    public void init() {
        eventHandler = new IssueEventHandler(connectionHandler, issueService, eventsHelper);
    }

    @Test
    public void testDeletedEvents() {

        when(dashboardService.getDashboardWithNames(anyList())).thenReturn(Collections.emptyList());

        eventHandler.processEvents(Collections.singletonList(new Event()), Collections.emptySet());

        verify(connectionHandler, times(1)).sendEventUpdateMessageToAll(EventType.ISSUE);
    }
}