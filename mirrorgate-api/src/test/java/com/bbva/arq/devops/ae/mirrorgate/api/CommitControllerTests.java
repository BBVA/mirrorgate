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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.*;
import com.bbva.arq.devops.ae.mirrorgate.dto.ScmDTO;
import com.bbva.arq.devops.ae.mirrorgate.service.CommitService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
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

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CommitController.class)
@WebAppConfiguration
public class CommitControllerTests {

    private static final String[] GIT_REPO_URLS = new String[]{
        "ssh://repo1.git",
        "ssh://repo2.git"
    };

    private MockMvc mockMvc = null;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private CommitService commitService;

    @MockBean
    private DashboardService dashboardService;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getScmMetricsByBoardNameTest() throws Exception {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();
        dashboard.setGitRepos(Arrays.asList(GIT_REPO_URLS));

        ScmDTO scmDTO = new ScmDTO()
            .setSecondsToMaster(259200D)
            .setCommitsPerDay(25D);

        when(dashboardService.getDashboard(dashboard.getName()))
            .thenReturn(dashboard);
        when(commitService.getScmStats(dashboard.getGitRepos()))
            .thenReturn(scmDTO);

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/scm_metrics"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.secondsToMaster", equalTo(259200D)))
            .andExpect(jsonPath("$.commitsPerDay", equalTo(25D)));
    }

    @Test
    public void getNullScmMetricsByBoardNameTest() throws Exception {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();
        dashboard.setGitRepos(Arrays.asList(GIT_REPO_URLS));

        ScmDTO scmDTO = new ScmDTO()
            .setSecondsToMaster(null)
            .setCommitsPerDay(null);

        when(dashboardService.getDashboard(dashboard.getName()))
            .thenReturn(dashboard);
        when(commitService.getScmStats(dashboard.getGitRepos()))
            .thenReturn(scmDTO);

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/scm_metrics"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.secondsToMaster", equalTo(null)))
            .andExpect(jsonPath("$.commitsPerDay", equalTo(null)));
    }

}