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
import com.bbva.arq.devops.ae.mirrorgate.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.dto.FailureTendency;
import com.bbva.arq.devops.ae.mirrorgate.repository.BuildRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.BuildStatus;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuildServiceTestsIT {

    @Autowired
    private BuildServiceImpl buildService;

    @Autowired
    private BuildRepository buildRepository;

    @Test
    public void createOrUpdateAddBuildsStatsProperly() {

        final String projectName = "MirrorGate";
        final String repositoryName = "mirrorgate";

        final List<String> keywords = Arrays.asList(projectName, repositoryName);

        final BuildDTO build1 = TestObjectFactory.createBuildDTO()
            .setProjectName(projectName)
            .setRepoName(repositoryName)
            .setTimestamp(TODAY);
        final BuildDTO build2 = TestObjectFactory.createBuildDTO()
            .setProjectName(projectName)
            .setRepoName(repositoryName)
            .setTimestamp(YESTERDAY);
        final BuildDTO build3 = TestObjectFactory.createBuildDTO()
            .setProjectName(projectName)
            .setRepoName(repositoryName)
            .setTimestamp(ONE_WEEK_AGO);
        final BuildDTO build4 = TestObjectFactory.createBuildDTO()
            .setProjectName(projectName)
            .setRepoName(repositoryName)
            .setTimestamp(ONE_MONTH_AGO);

        buildService.createOrUpdate(build1.setBuildStatus(BuildStatus.Success.name()));
        buildService.createOrUpdate(build1.setBuildStatus(BuildStatus.Failure.name()));
        buildService.createOrUpdate(build1.setBuildStatus(BuildStatus.Success.name()));

        buildService.createOrUpdate(build2.setBuildStatus(BuildStatus.Success.name()));
        buildService.createOrUpdate(build2.setBuildStatus(BuildStatus.Failure.name()));

        buildService.createOrUpdate(build3.setBuildStatus(BuildStatus.Success.name()));

        buildService.createOrUpdate(build4.setBuildStatus(BuildStatus.Success.name()));

        final BuildStats buildStats = buildService.getStatsAndTendenciesByKeywordsAndByTeamMembers(keywords, Collections.emptyList());

        assertThat(buildStats.getCount()).isEqualTo(2); // By default older than a week are discarted
        assertThat(buildStats.getDuration()).isEqualTo((build1.getDuration() + build2.getDuration()) / 2.0);
        assertThat(buildStats.getFailureRate()).isEqualTo(50.0);
        assertThat(buildStats.getFailureTendency()).isEqualTo(FailureTendency.up);
    }

    @After
    public void cleanUp() {
        buildRepository.deleteAll();
    }
}
