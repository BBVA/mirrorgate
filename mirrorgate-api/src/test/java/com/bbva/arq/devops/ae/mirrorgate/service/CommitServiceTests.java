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

import com.bbva.arq.devops.ae.mirrorgate.dto.ScmDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Commit;
import com.bbva.arq.devops.ae.mirrorgate.repository.CommitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
    public void getLastCommitByRepoNameTest() {
        Commit commit = new Commit();

        when(commitRepository.findOneByRepositoryOrderByTimestampDesc(GIT_REPO_URLS[0]))
            .thenReturn(commit);
        when(commitRepository.findOneByRepositoryOrderByTimestampDesc(GIT_REPO_URLS[1]))
            .thenReturn(null);

        String commit1 = commitService.getLastCommit(GIT_REPO_URLS[0]);
        String commit2 = commitService.getLastCommit(GIT_REPO_URLS[1]);

        assertThat(commit1).isEqualTo(commit.getHash());
        assertThat(commit2).isEqualTo(null);
    }

    @Test
    public void getScmStatsByRepoListTest() {
        ScmDTO scmDTO = new ScmDTO()
            .setSecondsToMaster(259200D)
            .setCommitsPerDay(25D);

        long now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long thirtyDaysAgo = now - (30 * 60 * 60 * 24);

        when(commitRepository.getSecondsToMaster(Arrays.asList(GIT_REPO_URLS), thirtyDaysAgo))
            .thenReturn(scmDTO.getSecondsToMaster());
        when(commitRepository.getCommitsPerDay(Arrays.asList(GIT_REPO_URLS), thirtyDaysAgo, 30))
            .thenReturn(scmDTO.getCommitsPerDay());

        ScmDTO scmStats = commitService.getScmStats(Arrays.asList(GIT_REPO_URLS));

        assertThat(scmStats).isEqualToComparingFieldByField(scmDTO);
    }
}
