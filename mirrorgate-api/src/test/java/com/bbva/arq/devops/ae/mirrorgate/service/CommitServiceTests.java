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
import static org.mockito.Mockito.when;

import com.bbva.arq.devops.ae.mirrorgate.dto.ScmDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Commit;
import com.bbva.arq.devops.ae.mirrorgate.repository.CommitRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CommitServiceTests {

    private static final String[] GIT_REPO_URLS = new String[]{
        "ssh://repo1.git",
        "ssh://repo2.git"
    };

    @Mock
    private CommitRepository commitRepository;

    @InjectMocks
    private CommitServiceImpl commitService;

    @Test
    public void getScmStatsByRepoListTest() {
        final ScmDTO scmDTO = new ScmDTO()
            .setSecondsToMaster(259200D)
            .setCommitsPerDay(25D);

        final long now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        final long thirtyDaysAgo = now - (30 * 60 * 60 * 24);

        when(commitRepository.getSecondsToMaster(Arrays.asList(GIT_REPO_URLS), thirtyDaysAgo))
            .thenReturn(scmDTO.getSecondsToMaster());
        when(commitRepository.getCommitsPerDay(Arrays.asList(GIT_REPO_URLS), thirtyDaysAgo, 30))
            .thenReturn(scmDTO.getCommitsPerDay());

        final ScmDTO scmStats = commitService.getScmStats(Arrays.asList(GIT_REPO_URLS));

        assertThat(scmStats).isEqualToComparingFieldByField(scmDTO);
    }

    @Test
    public void getLastCommitsByRepoNameTest() {
        final Commit commit = new Commit().setHash("1");

        when(commitRepository
            .findByRepositoryAndTimestampGreaterThanOrderByTimestampDesc(GIT_REPO_URLS[0], 1))
            .thenReturn(new ArrayList<>());
        when(commitRepository
            .findByRepositoryAndTimestampGreaterThanOrderByTimestampDesc(GIT_REPO_URLS[0], 2))
            .thenReturn(Collections.singletonList(commit));

        final List<String> commits1 = commitService.getLastCommits(GIT_REPO_URLS[0], 1);
        final List<String> commits2 = commitService.getLastCommits(GIT_REPO_URLS[0], 2);

        assertThat(commits1).isEqualTo(new ArrayList<String>());
        assertThat(commits2.get(0)).isEqualTo(commit.getHash());
    }
}
