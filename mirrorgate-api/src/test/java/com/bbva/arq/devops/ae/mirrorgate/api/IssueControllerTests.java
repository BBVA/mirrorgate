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

import static com.bbva.arq.devops.ae.mirrorgate.mapper.DashboardMapper.map;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.IssueService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebMvcTest(IssueController.class)
@WebAppConfiguration
public class IssueControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;
    @MockBean
    private IssueService issueService;
    @MockBean
    private DashboardService dashboardService;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getActiveUserStoriesTest() throws Exception {
        final String dashboardName = "mirrorgate";
        final String sprintProjectName = "mirrorgate";

        final Dashboard dashboard = new Dashboard()
            .setName(dashboardName)
            .setsProductName(sprintProjectName)
            .setBoards(Collections.singletonList(sprintProjectName));

        final Issue story1 = new Issue()
            .setIssueId("1")
            .setSprintAssetState("Active")
            .setProjectName(dashboardName);

        final Issue story2 = new Issue()
            .setIssueId("2")
            .setSprintAssetState("Active")
            .setProjectName(dashboardName);

        final List<Issue> stories = Arrays.asList(story1, story2);

        when(dashboardService.getDashboard(dashboardName)).thenReturn(map(dashboard));
        when(issueService.getActiveUserStoriesByBoards(Collections.singletonList(dashboardName)))
            .thenReturn(stories);

        this.mockMvc.perform(get("/dashboards/" + dashboardName + "/stories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentSprint[0].issueId", equalTo(story1.getIssueId())))
            .andExpect(jsonPath("$.currentSprint[0].sprintAssetState", equalTo(story1.getSprintAssetState())))
            .andExpect(jsonPath("$.currentSprint[0].projectName", equalTo(story1.getProjectName())))
            .andExpect(jsonPath("$.currentSprint[1].issueId", equalTo(story2.getIssueId())))
            .andExpect(jsonPath("$.currentSprint[1].sprintAssetState", equalTo(story2.getSprintAssetState())))
            .andExpect(jsonPath("$.currentSprint[1].projectName", equalTo(story2.getProjectName())));
    }
}