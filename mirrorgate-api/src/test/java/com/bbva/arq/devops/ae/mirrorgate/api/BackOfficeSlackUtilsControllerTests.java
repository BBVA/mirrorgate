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
package com.bbva.arq.devops.ae.mirrorgate.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.SlackDTO;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.SlackService;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import org.junit.Before;
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

@RunWith(SpringRunner.class)
@WebMvcTest(BackOfficeSlackUtilsController.class)
@WebAppConfiguration
public class BackOfficeSlackUtilsControllerTests {

    private MockMvc mockMvc = null;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private SlackService slackService;

    @MockBean
    private DashboardService dashboardService;

    private static final String SLACK_CODE = "SLACK_CODE";
    private static final String SLACK_DUMMY = "SLACK_DUMMY";

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void setSlackTokenTest() throws Exception {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();
        SlackDTO notification = TestObjectFactory.createSlackDTO();

        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);
        when(slackService.getToken(
                dashboard.getSlackTeam(),
                SLACK_DUMMY,
                SLACK_DUMMY,
                SLACK_CODE)).thenReturn(notification);

        this.mockMvc.perform(get("/backoffice/utils/slack/token-generator")
                .param("code", SLACK_CODE)
                .param("clientId", SLACK_DUMMY)
                .param("clientSecret", SLACK_DUMMY)
                .param("team", dashboard.getSlackTeam()))
                .andExpect(status().isOk());
    }

    @Test
    public void setSlackTokenSlackErrorTest() throws Exception {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();
        SlackDTO error_notification = TestObjectFactory.createSlackErrorDTO();

        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);
        when(slackService.getToken(
                dashboard.getSlackTeam(),
                SLACK_DUMMY,
                SLACK_DUMMY,
                SLACK_CODE)).thenReturn(error_notification);

        this.mockMvc.perform(get("/backoffice/utils/slack/token-generator")
                .param("code", SLACK_CODE)
                .param("clientId", SLACK_DUMMY)
                .param("clientSecret", SLACK_DUMMY)
                .param("team", dashboard.getSlackTeam()))
                .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

}
