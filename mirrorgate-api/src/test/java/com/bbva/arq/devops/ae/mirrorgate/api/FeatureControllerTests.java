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

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.FeatureService;
import org.bson.types.ObjectId;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.bbva.arq.devops.ae.mirrorgate.mapper.DashboardMapper.map;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FeatureController.class)
@WebAppConfiguration
public class FeatureControllerTests {

    private MockMvc mockMvc = null;

    @Autowired
    private WebApplicationContext wac;
    @MockBean
    private FeatureService featureService;
    @MockBean
    private DashboardService dashboardService;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getActiveUserStoriesTest() throws Exception {
        String dashboardName = "mirrorgate";
        String sprintProjectName = "mirrorgate";

        Dashboard dashboard = new Dashboard();
        dashboard.setId(ObjectId.get());
        dashboard.setName(dashboardName);
        dashboard.setsProductName(sprintProjectName);
        dashboard.setBoards(Collections.singletonList(sprintProjectName));

        Feature story1 = new Feature();
        story1.setId(ObjectId.get());
        story1.setsId("1");
        story1.setsSprintAssetState("Active");
        story1.setsProjectName(dashboardName);

        Feature story2 = new Feature();
        story2.setId(ObjectId.get());
        story2.setsId("2");
        story2.setsSprintAssetState("Active");
        story2.setsProjectName(dashboardName);

        List<Feature> stories = new ArrayList<>();
        stories.add(story1);
        stories.add(story2);

        when(dashboardService.getDashboard(dashboardName)).thenReturn(map(dashboard));
        when(featureService.getActiveUserStoriesByBoards(Collections.singletonList(dashboardName)))
            .thenReturn(stories);

        this.mockMvc.perform(get("/dashboards/" + dashboardName + "/stories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentSprint[0].sId", equalTo(story1.getsId())))
            .andExpect(jsonPath("$.currentSprint[0].sSprintAssetState", equalTo(story1.getsSprintAssetState())))
            .andExpect(jsonPath("$.currentSprint[0].sProjectName", equalTo(story1.getsProjectName())))
            .andExpect(jsonPath("$.currentSprint[1].sId", equalTo(story2.getsId())))
            .andExpect(jsonPath("$.currentSprint[1].sSprintAssetState", equalTo(story2.getsSprintAssetState())))
            .andExpect(jsonPath("$.currentSprint[1].sProjectName", equalTo(story2.getsProjectName())));
    }
}