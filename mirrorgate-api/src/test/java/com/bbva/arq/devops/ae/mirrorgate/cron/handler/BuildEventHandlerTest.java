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

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BuildEventHandlerTest {

    @MockBean
    private ConnectionHandler connectionHandler;

    @MockBean
    private BuildService buildService;

    @MockBean
    private DashboardService dashboardService;

    private BuildEventHandler eventHandler;

    @Before
    public void init() {
        final ProcessEventsHelper eventsHelper = new ProcessEventsHelper(dashboardService, connectionHandler);
        eventHandler = new BuildEventHandler(connectionHandler, buildService, eventsHelper);
    }

    @Test
    public void testBuildEvents() {
        final Dashboard dashboard1 = new Dashboard()
            .setName("dashboard1")
            .setCodeRepos(Arrays.asList("http://repo1.git", "http://repo2.git"));
        final Dashboard dashboard2 = new Dashboard()
            .setName("dashboard2")
            .setCodeRepos(Arrays.asList(".*repo3.*", "http://repo4.git"));
        final Dashboard dashboard3 = new Dashboard()
            .setName("dashboard3")
            .setCodeRepos(Collections.singletonList("http://repo3.git"));

        final BuildDTO build = TestObjectFactory.createBuildDTO()
            .setKeywords(Collections.singletonList("http://repo3.git"));

        final Event event = new Event().setCollectionId(build.getBuildUrl());
        final Set<String> dashboardIds = new HashSet<>(
            Arrays.asList(dashboard1.getName(), dashboard2.getName(), dashboard3.getName())
        );

        when(buildService.getBuildsByIds(anyList())).thenReturn(Collections.singletonList(build));
        when(dashboardService.getDashboardWithNames(anyList()))
            .thenReturn(Arrays.asList(dashboard1, dashboard2, dashboard3));

        eventHandler.processEvents(Collections.singletonList(event), dashboardIds);

        verify(connectionHandler, times(0))
            .sendEventUpdateMessage(EventType.BUILD, dashboard1.getName());
        verify(connectionHandler, times(1))
            .sendEventUpdateMessage(EventType.BUILD, dashboard2.getName());
        verify(connectionHandler, times(1))
            .sendEventUpdateMessage(EventType.BUILD, dashboard3.getName());
    }

    @Test
    public void testBuildWithTeamMembersEvents() {
        final Dashboard dashboard1 = new Dashboard()
            .setName("dashboard1")
            .setCodeRepos(Arrays.asList(".*repo1.*", "http://repo2.git"))
            .setTeamMembers(Collections.singletonList("Atreyu"));
        final Dashboard dashboard2 = new Dashboard()
            .setName("dashboard2")
            .setCodeRepos(Arrays.asList(".*repo1.*", "http://repo4.git"))
            .setTeamMembers(Arrays.asList("Atreyu", "Gmork"));

        final BuildDTO build = TestObjectFactory.createBuildDTO()
            .setKeywords(Collections.singletonList("http://repo1.git"))
            .setCulprits(Collections.singletonList("Gmork"));

        final Event event = new Event().setCollectionId(build.getBuildUrl());
        final Set<String> dashboardIds = new HashSet<>(Arrays.asList(dashboard1.getName(), dashboard2.getName()));

        when(buildService.getBuildsByIds(anyList())).thenReturn(Collections.singletonList(build));
        when(dashboardService.getDashboardWithNames(anyList())).thenReturn(Arrays.asList(dashboard1, dashboard2));

        eventHandler.processEvents(Collections.singletonList(event), dashboardIds);

        verify(connectionHandler, times(0))
            .sendEventUpdateMessage(EventType.BUILD, dashboard1.getName());
        verify(connectionHandler, times(1))
            .sendEventUpdateMessage(EventType.BUILD, dashboard2.getName());
    }
}