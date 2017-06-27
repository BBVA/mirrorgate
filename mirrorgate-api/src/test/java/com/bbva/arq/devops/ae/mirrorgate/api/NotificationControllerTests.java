/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bbva.arq.devops.ae.mirrorgate.api;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.SlackDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.SlackService;
import com.bbva.arq.devops.ae.mirrorgate.util.TestObjectBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 *
 * @author enrique
 */
@RunWith(SpringRunner.class)
@WebMvcTest(NotificationController.class)
@WebAppConfiguration
@Ignore
public class NotificationControllerTests {

    private MockMvc mockMvc = null;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private SlackService slackService;

    @MockBean
    private DashboardService dashboardService;

    private static final String SLACK_CODE = "SLACK_CODE";

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void setSlackTokenTest() throws Exception {
        Dashboard dashboard = TestObjectBuilder.createDashboard();
        SlackDTO notification = TestObjectBuilder.createSlackDTO();

        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);
        when(slackService.getToken(
                dashboard.getSlackTeam(),
                "dummy",
                "dummy",
                SLACK_CODE)).thenReturn(notification);

        this.mockMvc.perform(get("/utils/slack-token-generator")
                .param("code", SLACK_CODE))
                .andExpect(status().isOk());
    }

    @Test
    public void setSlackTokenFakeDashboardTest() throws Exception {
        String name = "fake";
        when(dashboardService.getDashboard(name)).thenReturn(null);

        verify(slackService, never()).getToken(any(), any(), any(), any());

        this.mockMvc.perform(get("/dashboards/" + name + "/slack")
                .param("code", SLACK_CODE))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void setSlackTokenSlackErrorTest() throws Exception {
        Dashboard dashboard = TestObjectBuilder.createDashboard();
        SlackDTO error_notification = TestObjectBuilder.createSlackErrorDTO();

        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);
        when(slackService.getToken(
                dashboard.getSlackTeam(),
                any(),
                any(),
                SLACK_CODE)).thenReturn(error_notification);

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/slack")
                .param("code", SLACK_CODE))
                .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

    @Test
    public void getWebSocketTest() throws Exception {
        Dashboard dashboard = TestObjectBuilder.createDashboard();
        SlackDTO notification = TestObjectBuilder.createSlackDTO();

        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);
        when(slackService.getWebSocket(
                dashboard.getSlackTeam(),
                any()
        )).thenReturn(notification);

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/notifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", is(notification.getUrl())));
    }

    @Test
    public void getWebSocketFakeDashboardTest() throws Exception {
        String name = "fake";
        when(dashboardService.getDashboard(name)).thenReturn(null);

        verify(slackService, never()).getWebSocket(any(), any());

        this.mockMvc.perform(get("/dashboards/" + name + "/notifications"))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getWebSocketSlackErrorTest() throws Exception {
        Dashboard dashboard = TestObjectBuilder.createDashboard();
        SlackDTO error_notification = TestObjectBuilder.createSlackErrorDTO();

        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);
        when(slackService.getWebSocket(
                dashboard.getSlackTeam(),
                any()
        )).thenReturn(error_notification);

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/notifications"))
                .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

}
