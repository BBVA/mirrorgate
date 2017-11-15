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
package com.bbva.arq.devops.ae.mirrorgate.service;

import static com.bbva.arq.devops.ae.mirrorgate.builders.BuildBuilder.makeBuild;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.repository.BuildRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BuildServiceTests {

    @Mock
    private BuildRepository buildRepository;

    @Mock
    private EventService eventService;

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private BuildServiceImpl buildService;


    @Test
    public void getAllBranchesLastByRepoName() {
        String repoName = "mirrorgate-app";

        Build build1 = makeBuild();
        build1.setRepoName(repoName);
        build1.setBranch("develop");

        Build build2 = makeBuild();
        build2.setRepoName(repoName);
        build2.setBranch("master");

        when(buildRepository.findLastBuildsByKeywordsAndByTeamMembers(Arrays.asList(repoName), null))
                .thenReturn(Arrays.asList(build1, build2));

        List<Build> lastBuilds = buildService
                .getLastBuildsByKeywordsAndByTeamMembers(Arrays.asList(repoName), null);
        verify(buildRepository, times(1))
                .findLastBuildsByKeywordsAndByTeamMembers(Arrays.asList(repoName), null);

        assertThat(lastBuilds.get(1).getId())
                .isEqualTo(build2.getId());
        assertThat(lastBuilds.get(1).getTimestamp())
                .isEqualTo(build2.getTimestamp());
        assertThat(lastBuilds.get(1).getBuildStatus())
                .isEqualTo(build2.getBuildStatus());
    }

    @Test
    public void getLastByRepoNameAndByTeamMembers() {
        String repoName = "mirrorgate-app";

        Build build1 = makeBuild();
        build1.setRepoName(repoName);
        build1.setBranch("develop");
        build1.setCulprits(Arrays.asList("Atreyu"));

        Build build2 = makeBuild();
        build2.setRepoName(repoName);
        build2.setBranch("master");
        build1.setCulprits(Arrays.asList("Atreyu"));

        Build build3 = makeBuild();
        build3.setRepoName(repoName);
        build3.setBranch("master");

        when(buildRepository.findLastBuildsByKeywordsAndByTeamMembers(Arrays.asList(repoName), Arrays.asList("Atreyu")))
                .thenReturn(Arrays.asList(build2));

        List<Build> lastBuilds = buildService
                .getLastBuildsByKeywordsAndByTeamMembers(Arrays.asList(repoName), Arrays.asList("Atreyu"));
        verify(buildRepository, times(1))
                .findLastBuildsByKeywordsAndByTeamMembers(Arrays.asList(repoName), Arrays.asList("Atreyu"));

        assertThat(lastBuilds.get(0).getId())
                .isEqualTo(build2.getId());
        assertThat(lastBuilds.get(0).getTimestamp())
                .isEqualTo(build2.getTimestamp());
        assertThat(lastBuilds.get(0).getBuildStatus())
                .isEqualTo(build2.getBuildStatus());
    }

    @Test
    public void createBuildTest() {
        Build build = makeBuild();
        BuildDTO request = TestObjectFactory.createBuildDTO();

        when(dashboardService.getDashboard(anyString())).thenReturn(new DashboardDTO());
        when(buildRepository.save((Build)any())).thenReturn(build);

        BuildDTO b = buildService.createOrUpdate(request);

        verify(buildRepository, times(1)).save((Build)any());
        verify(dashboardService, times(1)).newDashboard((DashboardDTO) any());

        assertThat(b.getBuildUrl()).isEqualTo(build.getBuildUrl());
    }
}
