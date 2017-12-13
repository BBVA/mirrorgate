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

import static org.assertj.core.api.Assertions.assertThat;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.BuildSummary;
import com.bbva.arq.devops.ae.mirrorgate.repository.BuildSummaryRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuildServiceTestsIT {

    @Autowired
    private BuildServiceImpl buildService;

    @Autowired
    private BuildSummaryRepository buildSummaryRepository;

    private static final long TODAY = LocalDateTime.now(ZoneId.of("UTC")).toInstant(ZoneOffset.UTC).toEpochMilli();
    private static final long YESTERDAY = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1).toInstant(ZoneOffset.UTC).toEpochMilli();
    private static final long ONE_WEEK_AGO = LocalDateTime.now(ZoneId.of("UTC")).minusDays(7).toInstant(ZoneOffset.UTC).toEpochMilli();

    @Test
    public void createOrUpdateAddBuildsStatsProperly() {
        BuildDTO request1 = TestObjectFactory
                .createBuildDTO()
                .setTimestamp(TODAY);

        BuildDTO request2 = TestObjectFactory
                .createBuildDTO()
                .setTimestamp(YESTERDAY);

        BuildDTO request3 = TestObjectFactory
                .createBuildDTO()
                .setTimestamp(ONE_WEEK_AGO);

        buildService.createOrUpdate(request1);
        buildService.createOrUpdate(request1);
        buildService.createOrUpdate(request1);

        buildService.createOrUpdate(request2);
        buildService.createOrUpdate(request2);

        buildService.createOrUpdate(request3);

        BuildSummary buildSummary1 = buildSummaryRepository.findByRepoNameAndProjectNameAndTimestamp(request1.getRepoName(), request1.getProjectName(), LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS));
        BuildSummary buildSummary2 = buildSummaryRepository.findByRepoNameAndProjectNameAndTimestamp(request2.getRepoName(), request2.getProjectName(), LocalDateTimeHelper.getTimestampPeriod(YESTERDAY, ChronoUnit.DAYS));
        BuildSummary buildSummary3 = buildSummaryRepository.findByRepoNameAndProjectNameAndTimestamp(request3.getRepoName(), request3.getProjectName(), LocalDateTimeHelper.getTimestampPeriod(ONE_WEEK_AGO, ChronoUnit.DAYS));

        assertThat(buildSummary1.getTotalBuilds()).isEqualTo(3);
        assertThat(buildSummary1.getTotalDuration()).isEqualTo(request1.getDuration() * 3);
        assertThat(buildSummary2.getTotalBuilds()).isEqualTo(2);
        assertThat(buildSummary3.getTotalBuilds()).isEqualTo(1);
    }
}
