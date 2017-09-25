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

import static com.bbva.arq.devops.ae.mirrorgate.builders.BuildBuilder.makeBuild;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.FailureTendency;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import com.bbva.arq.devops.ae.mirrorgate.support.TestUtil;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebMvcTest(BuildController.class)
@WebAppConfiguration
public class BuildControllerTests {

    private static final String[] REPO_NAMES = new String[]{
        "http://repo1.git",
        "http://repo2.git"
    };

    private MockMvc mockMvc = null;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private BuildService buildService;

    @MockBean
    private DashboardService dashboardService;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getBuildsByDashboardNameTest() throws Exception {
        Dashboard dashboard = TestObjectFactory.createDashboard();
        dashboard.setCodeRepos(Arrays.asList(REPO_NAMES));
        Build build1 = makeBuild(REPO_NAMES[0]);
        Build build2 = makeBuild(REPO_NAMES[1]);

        when(dashboardService.getDashboard(dashboard.getName()))
                .thenReturn(dashboard);
        when(buildService.getLastBuildsByReposNameAndByTeamMembers(dashboard.getCodeRepos(), dashboard.getTeamMembers()))
                .thenReturn(Arrays.asList(build1, build2));

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/builds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastBuilds[0].repoName", equalTo(build1.getRepoName())))
                .andExpect(jsonPath("$.lastBuilds[1].repoName", equalTo(build2.getRepoName())));
    }

    @Test
    public void getFailureRateTest() throws Exception {
        Dashboard dashboard = TestObjectFactory.createDashboard();
        dashboard.setCodeRepos(Arrays.asList(REPO_NAMES));
        BuildStats buildStats = new BuildStats()
                .setDuration(0)
                .setCount(3)
                .setFailureRate(33)
                .setFailureTendency(FailureTendency.equal);

        when(dashboardService.getDashboard(dashboard.getName()))
                .thenReturn(dashboard);
        when(buildService.getStatsFromReposByTeamMembers(eq(dashboard.getCodeRepos()), eq(dashboard.getTeamMembers())))
                .thenReturn(buildStats);

        this.mockMvc.perform(
                get("/dashboards/" + dashboard.getName() + "/builds/rate"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"duration\":0.0,\"count\":3,\""
                        + "failureRate\":33,\"failureTendency\":\"equal\"}"));
    }

    @Test
    public void getFailureRateWithoutBuildsTest() throws Exception {
        Dashboard dashboard = TestObjectFactory.createDashboard();
        dashboard.setCodeRepos(Arrays.asList(REPO_NAMES));
        BuildStats buildStats = new BuildStats()
                .setDuration(0)
                .setCount(0)
                .setFailureRate(0)
                .setFailureTendency(FailureTendency.equal);

        when(dashboardService.getDashboard(dashboard.getName()))
                .thenReturn(dashboard);
        when(buildService.getStatsFromReposByTeamMembers(eq(dashboard.getCodeRepos()), eq(dashboard.getTeamMembers())))
                .thenReturn(buildStats);

        this.mockMvc.perform(
                get("/dashboards/" + dashboard.getName() + "/builds/rate"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"duration\":0.0,\"count\":0,\""
                        + "failureRate\":0,\"failureTendency\":\"equal\"}"));
    }

    @Test
    public void getFailureRateWithoutFailureBuildsTest() throws Exception {
        Dashboard dashboard = TestObjectFactory.createDashboard();
        dashboard.setCodeRepos(Arrays.asList(REPO_NAMES));
        BuildStats buildStats = new BuildStats()
                .setCount(3)
                .setDuration(0)
                .setFailureRate(0)
                .setFailureTendency(FailureTendency.equal);

        when(dashboardService.getDashboard(dashboard.getName()))
                .thenReturn(dashboard);
        when(buildService.getStatsFromReposByTeamMembers(eq(dashboard.getCodeRepos()), eq(dashboard.getTeamMembers())))
                .thenReturn(buildStats);

        this.mockMvc.perform(
                get("/dashboards/" + dashboard.getName() + "/builds/rate"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"duration\":0.0,\"count\":3,\""
                        + "failureRate\":0,\"failureTendency\":\"equal\"}"));
    }

    @Test
    public void createBuildTest() throws Exception {
        BuildDTO request = TestObjectFactory.createBuildDTO();
        when(buildService.createOrUpdate(Matchers.any(BuildDTO.class)))
                .thenReturn("123456");
        this.mockMvc.perform(post("/api/builds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isCreated());
    }

}