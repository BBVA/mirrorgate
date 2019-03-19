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

import com.bbva.arq.devops.ae.mirrorgate.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.repository.BuildRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.bbva.arq.devops.ae.mirrorgate.builders.BuildBuilder.makeBuild;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        final String repoName = "mirrorgate-app";

        final Build build1 = makeBuild()
            .setRepoName(repoName)
            .setBranch("develop");

        final Build build2 = makeBuild()
            .setRepoName(repoName)
            .setBranch("master");

        when(
            buildRepository.findLastBuildsByKeywordsAndByTeamMembers(
                Collections.singletonList(repoName),
                null
            )
        ).thenReturn(Arrays.asList(build1, build2));

        final List<Build> lastBuilds = buildService
            .getLastBuildsByKeywordsAndByTeamMembers(Collections.singletonList(repoName), null);
        verify(buildRepository, times(1))
            .findLastBuildsByKeywordsAndByTeamMembers(Collections.singletonList(repoName), null);

        assertThat(lastBuilds.get(1).getId()).isEqualTo(build2.getId());
        assertThat(lastBuilds.get(1).getTimestamp()).isEqualTo(build2.getTimestamp());
        assertThat(lastBuilds.get(1).getBuildStatus()).isEqualTo(build2.getBuildStatus());
    }

    @Test
    public void getLastByRepoNameAndByTeamMembers() {
        final String repoName = "mirrorgate-app";

        final Build build = makeBuild()
            .setRepoName(repoName)
            .setBranch("master")
            .setCulprits(Collections.singletonList("Atreyu"));

        when(buildRepository.findLastBuildsByKeywordsAndByTeamMembers(
            Collections.singletonList(repoName),
            Collections.singletonList("Atreyu"))
        ).thenReturn(Collections.singletonList(build));

        final List<Build> lastBuilds = buildService.getLastBuildsByKeywordsAndByTeamMembers(
            Collections.singletonList(repoName), Collections.singletonList("Atreyu"));
        verify(buildRepository, times(1)).findLastBuildsByKeywordsAndByTeamMembers(
            Collections.singletonList(repoName), Collections.singletonList("Atreyu"));

        assertThat(lastBuilds.get(0).getId()).isEqualTo(build.getId());
        assertThat(lastBuilds.get(0).getTimestamp()).isEqualTo(build.getTimestamp());
        assertThat(lastBuilds.get(0).getBuildStatus()).isEqualTo(build.getBuildStatus());
    }

    @Test
    public void createBuildTest() {
        final Build build = makeBuild();
        final BuildDTO request = TestObjectFactory.createBuildDTO();

        when(dashboardService.getDashboard(anyString())).thenReturn(new DashboardDTO());
        when(buildRepository.save(any())).thenReturn(build);

        final BuildDTO b = buildService.createOrUpdate(request);

        verify(buildRepository, times(1)).save(any());
        verify(dashboardService, times(1)).createDashboardForBuildProject(any());

        assertThat(b.getBuildUrl()).isEqualTo(build.getBuildUrl());
    }
}
