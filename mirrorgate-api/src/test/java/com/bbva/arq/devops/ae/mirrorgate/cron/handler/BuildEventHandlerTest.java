package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        ProcessEventsHelper eventsHelper = new ProcessEventsHelper(dashboardService, connectionHandler);
        eventHandler = new BuildEventHandler(connectionHandler, buildService, eventsHelper);
    }

    @Test
    public void testBuildEvents() {
        Dashboard dashboard1 = new Dashboard()
            .setName("dashboard1")
            .setCodeRepos(Arrays.asList("http://repo1.git", "http://repo2.git"));
        Dashboard dashboard2 = new Dashboard()
            .setName("dashboard2")
            .setCodeRepos(Arrays.asList(".*repo3.*", "http://repo4.git"));
        Dashboard dashboard3 = new Dashboard()
            .setName("dashboard3")
            .setCodeRepos(Collections.singletonList("http://repo3.git"));

        BuildDTO build = TestObjectFactory.createBuildDTO()
            .setKeywords(Collections.singletonList("http://repo3.git"));

        Event event = new Event().setCollectionId(build.getBuildUrl());
        Set<String> dashboardIds = new HashSet<>(Arrays.asList(dashboard1.getName(), dashboard2.getName(), dashboard3.getName()));

        when(buildService.getBuildsByIds(anyList())).thenReturn(Collections.singletonList(build));
        when(dashboardService.getDashboardWithNames(anyList())).thenReturn(Arrays.asList(dashboard1, dashboard2, dashboard3));

        eventHandler.processEvents(Collections.singletonList(event), dashboardIds);

        verify(connectionHandler, times(0)).sendEventUpdateMessage(EventType.BUILD, dashboard1.getName());
        verify(connectionHandler, times(1)).sendEventUpdateMessage(EventType.BUILD, dashboard2.getName());
        verify(connectionHandler, times(1)).sendEventUpdateMessage(EventType.BUILD, dashboard3.getName());
    }

    @Test
    public void testBuildWithTeamMembersEvents() {
        Dashboard dashboard1 = new Dashboard()
            .setName("dashboard1")
            .setCodeRepos(Arrays.asList(".*repo1.*", "http://repo2.git"))
            .setTeamMembers(Collections.singletonList("Atreyu"));
        Dashboard dashboard2 = new Dashboard()
            .setName("dashboard2")
            .setCodeRepos(Arrays.asList(".*repo1.*", "http://repo4.git"))
            .setTeamMembers(Arrays.asList("Atreyu", "Gmork"));

        BuildDTO build = TestObjectFactory.createBuildDTO()
            .setKeywords(Collections.singletonList("http://repo1.git"))
            .setCulprits(Collections.singletonList("Gmork"));

        Event event = new Event().setCollectionId(build.getBuildUrl());
        Set<String> dashboardIds = new HashSet<>(Arrays.asList(dashboard1.getName(), dashboard2.getName()));

        when(buildService.getBuildsByIds(anyList())).thenReturn(Collections.singletonList(build));
        when(dashboardService.getDashboardWithNames(anyList())).thenReturn(Arrays.asList(dashboard1, dashboard2));

        eventHandler.processEvents(Collections.singletonList(event), dashboardIds);

        verify(connectionHandler, times(0)).sendEventUpdateMessage(EventType.BUILD, dashboard1.getName());
        verify(connectionHandler, times(1)).sendEventUpdateMessage(EventType.BUILD, dashboard2.getName());
    }

}
