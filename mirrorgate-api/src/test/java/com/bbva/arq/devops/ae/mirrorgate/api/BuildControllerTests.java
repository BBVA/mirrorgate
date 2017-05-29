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
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.BuildStatus;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.util.TestUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static final String DASHBOARD_NAME = "mirrorgate";
    private static final String[] REPO_NAMES = new String[]{
            "http://repo1.git",
            "http://repo2.git"
    };
    private static final List<String> REPO_NAMES_LIST = Arrays.asList(REPO_NAMES);

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
        Build build1 = makeBuild(REPO_NAMES[0]);
        Build build2 = makeBuild(REPO_NAMES[1]);

        when(dashboardService.getReposByDashboardName(DASHBOARD_NAME)).thenReturn(REPO_NAMES_LIST);
        when(buildService.getAllBranchesLastByReposName(REPO_NAMES_LIST)).thenReturn(Arrays.asList(build1,build2));

        this.mockMvc.perform(get("/dashboards/" + DASHBOARD_NAME + "/builds"))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$.lastBuilds[0].timestamp", equalTo((int) build1.getTimestamp())))
            .andExpect(jsonPath("$.lastBuilds[0].buildStatus", equalTo(build1.getBuildStatus().toString())))
            .andExpect(jsonPath("$.lastBuilds[1].timestamp", equalTo((int) build2.getTimestamp())))
            .andExpect(jsonPath("$.lastBuilds[1].buildStatus", equalTo(build2.getBuildStatus().toString())));
    }

    @Test
    public void getFailureRateTest() throws Exception {
        Map<BuildStatus, BuildStats> stats = new HashMap<>();
        stats.put(BuildStatus.Success, new BuildStats().setCount(2));
        stats.put(BuildStatus.Failure, new BuildStats().setCount(1).setFailureRate(100));

        when(dashboardService.getReposByDashboardName(DASHBOARD_NAME)).thenReturn(REPO_NAMES_LIST);
        when(buildService.getBuildStatusStatsAfterTimestamp(eq(REPO_NAMES_LIST), anyLong()))
                .thenReturn(stats);

        this.mockMvc.perform(get("/dashboards/" + DASHBOARD_NAME + "/builds/rate"))
                .andExpect(status().isOk())
            .andExpect(content().string("{\"duration\":0.0,\"count\":3,\"failureRate\":33}"));
    }

    @Test
    public void getFailureRateWithoutBuildsTest() throws Exception {
        when(dashboardService.getReposByDashboardName(DASHBOARD_NAME)).thenReturn(REPO_NAMES_LIST);
        when(buildService.getBuildStatusStatsAfterTimestamp(eq(REPO_NAMES_LIST), anyLong()))
                .thenReturn(new HashMap<>());

        this.mockMvc.perform(get("/dashboards/" + DASHBOARD_NAME + "/builds/rate"))
                .andExpect(status().isOk())
            .andExpect(content().string("{\"duration\":0.0,\"count\":0,\"failureRate\":0}"));
    }

    @Test
    public void getFailureRateWithoutFailureBuildsTest() throws Exception {
        Map<BuildStatus, BuildStats> stats = new HashMap<>();
        stats.put(BuildStatus.Success, new BuildStats().setCount(3));

        when(dashboardService.getReposByDashboardName(DASHBOARD_NAME)).thenReturn(REPO_NAMES_LIST);
        when(buildService.getBuildStatusStatsAfterTimestamp(eq(REPO_NAMES_LIST), anyLong()))
                .thenReturn(stats);

        this.mockMvc.perform(get("/dashboards/" + DASHBOARD_NAME + "/builds/rate"))
                .andExpect(status().isOk())
            .andExpect(content().string("{\"duration\":0.0,\"count\":3,\"failureRate\":0}"));
    }

    @Test
    public void createBuildTest() throws Exception {
        BuildDTO request = makeBuildRequest();
        when(buildService.createOrUpdate(Matchers.any(BuildDTO.class))).thenReturn("123456");
        this.mockMvc.perform(post("/api/builds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isCreated());
    }

    private BuildDTO makeBuildRequest() {
        BuildDTO build = new BuildDTO();
        build.setNumber("1");
        build.setBuildUrl("buildUrl");
        build.setStartTime(3);
        build.setEndTime(8);
        build.setDuration(5);
        build.setBuildStatus("Success");
        build.setStartedBy("foo");
        build.setProjectName("mirrorgate");
        build.setRepoName("api");
        build.setBranch("test");
        return build;
    }

}