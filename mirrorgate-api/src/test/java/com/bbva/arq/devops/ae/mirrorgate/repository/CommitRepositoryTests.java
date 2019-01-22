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
package com.bbva.arq.devops.ae.mirrorgate.repository;

import com.bbva.arq.devops.ae.mirrorgate.model.Commit;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CommitRepositoryTests {

    private static final int ONE_DAY_AGO = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (24 * 60 * 60);
    private static final int SEVEN_DAYS_AGO = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (7 * 24 * 60 * 60);
    private static final int FIFTEEN_DAYS_AGO = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (15 * 24 * 60 * 60);
    private static final int ONE_MONTH_AGO = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (30 * 24 * 60 * 60);

    private static final String[] GIT_REPO_URLS = new String[]{
        "ssh://fake.server.com/mirrorgate/repo1.git",
        "mirrorgate/repo1.git",
        "ssh://fake.server.com/mirrorgate/repo2.git",
        "ssh://fake.server.com/mirrorgate/repo3.git"
    };

    @Autowired
    private CommitRepository repository;

    @Test
    public void getSecondsToMasterByRepoListTest() {
        List<String> repos = Arrays.asList(GIT_REPO_URLS);
        int timestamp1 = ONE_DAY_AGO - (60 * 60);
        int timestamp2 = ONE_DAY_AGO - (2 * 60 * 60);

        HashMap<String, Integer> branches1 = new HashMap<String, Integer>(){
            {
                put("refs/remotes/origin/master", ONE_DAY_AGO);
            }
        };

        HashMap<String, Integer> branches2 = new HashMap<String, Integer>(){
            {
                put("refs/remotes/origin/master", ONE_DAY_AGO);
            }
        };

        Commit commit1 = new Commit().setRepository(repos.get(0)).setTimestamp(timestamp1).setBranches(branches1);
        Commit commit2 = new Commit().setRepository(repos.get(1)).setTimestamp(timestamp2).setBranches(branches2);

        repository.saveAll(Arrays.asList(commit1, commit2));

        long now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long thirtyDaysAgo = now - (30 * 60 * 60 * 24);

        Double seconds1 = repository.getSecondsToMaster(Collections.singletonList(repos.get(0)), thirtyDaysAgo);
        Double seconds2 = repository.getSecondsToMaster(repos, thirtyDaysAgo);
        Double seconds3 = repository.getSecondsToMaster(Collections.singletonList(repos.get(2)), thirtyDaysAgo);

        assertThat(seconds1).isEqualTo(ONE_DAY_AGO - timestamp1);
        assertThat(seconds2).isEqualTo(((ONE_DAY_AGO - timestamp1) + (ONE_DAY_AGO - timestamp2)) / 2.0);
        assertThat(seconds3).isEqualTo(null);
    }

    @Test
    public void getSecondsToMasterByRepoPathListTest() {
        int timestamp = FIFTEEN_DAYS_AGO - (60 * 60);

        HashMap<String, Integer> branches1 = new HashMap<String, Integer>(){
            {
                put("refs/remotes/origin/master", ONE_DAY_AGO);
            }
        };

        HashMap<String, Integer> branches2 = new HashMap<String, Integer>(){
            {
                put("refs/remotes/origin/master", SEVEN_DAYS_AGO);
            }
        };

        Commit commit1 = new Commit().setRepository(Arrays.asList(GIT_REPO_URLS).get(0)).setTimestamp(timestamp).setBranches(branches1);
        Commit commit2 = new Commit().setRepository(Arrays.asList(GIT_REPO_URLS).get(2)).setTimestamp(timestamp).setBranches(branches2);

        repository.saveAll(Arrays.asList(commit1, commit2));

        long now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long thirtyDaysAgo = now - (30 * 60 * 60 * 24);

        Double seconds = repository.getSecondsToMaster(Collections.singletonList(GIT_REPO_URLS[1]), thirtyDaysAgo);

        assertThat(seconds).isNotNull();
        assertThat(seconds).isEqualTo(ONE_DAY_AGO - timestamp);
    }

    @Test
    public void getCommitsPerDayByRepoPathListTest() {
        List<String> repos = Arrays.asList(GIT_REPO_URLS);

        Commit commit1 = new Commit().setRepository(repos.get(0)).setTimestamp(ONE_DAY_AGO);
        Commit commit2 = new Commit().setRepository(repos.get(0)).setTimestamp(ONE_DAY_AGO);
        Commit commit3 = new Commit().setRepository(repos.get(0)).setTimestamp(ONE_DAY_AGO);
        Commit commit4 = new Commit().setRepository(repos.get(2)).setTimestamp(ONE_DAY_AGO);
        Commit commit5 = new Commit().setRepository(repos.get(2)).setTimestamp(ONE_DAY_AGO);
        Commit commit6 = new Commit().setRepository(repos.get(2)).setTimestamp(ONE_DAY_AGO);
        Commit commit7 = new Commit().setRepository(repos.get(2)).setTimestamp(SEVEN_DAYS_AGO);

        repository.saveAll(Arrays.asList(commit1, commit2, commit3, commit4, commit5, commit6, commit7));

        long now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long threeDaysAgo = now - (3 * 60 * 60 * 24);
        long thirtyDaysAgo = now - (30 * 60 * 60 * 24);

        Double commits1 = repository.getCommitsPerDay(Collections.singletonList(repos.get(0)), thirtyDaysAgo, 30);
        Double commits2 = repository.getCommitsPerDay(Collections.singletonList(repos.get(1)), thirtyDaysAgo, 30);
        Double commits3 = repository.getCommitsPerDay(Collections.singletonList(repos.get(2)), thirtyDaysAgo, 30);
        Double commits4 = repository.getCommitsPerDay(repos, thirtyDaysAgo, 30);
        Double commits5 = repository.getCommitsPerDay(repos, threeDaysAgo, 3);
        Double commits6 = repository.getCommitsPerDay(Collections.singletonList(repos.get(3)), thirtyDaysAgo, 30);

        assertThat(commits1).isEqualTo(3D / 30D);
        assertThat(commits2).isEqualTo(3D / 30D);
        assertThat(commits3).isEqualTo(4D / 30D);
        assertThat(commits4).isEqualTo(7D / 30D);
        assertThat(commits5).isEqualTo(6D / 3D);
        assertThat(commits6).isEqualTo(null);

    }

    @Test
    public void getCommitsByRepoAndTimestampGreaterThanTest(){
        String repo = GIT_REPO_URLS[0];

        Commit commit1 = new Commit().setRepository(repo).setHash("1").setTimestamp(SEVEN_DAYS_AGO);
        Commit commit2 = new Commit().setRepository(repo).setHash("2").setTimestamp(ONE_DAY_AGO);
        Commit commit3 = new Commit().setRepository(repo).setHash("3").setTimestamp(FIFTEEN_DAYS_AGO);
        Commit commit4 = new Commit().setRepository(repo).setHash("4").setTimestamp(ONE_MONTH_AGO);

        repository.saveAll(Arrays.asList(commit1, commit2, commit3, commit4));

        List<Commit> lastCommits = repository.findByRepositoryAndTimestampGreaterThanOrderByTimestampDesc(repo, FIFTEEN_DAYS_AGO);

        assertThat(lastCommits.size()).isEqualTo(2);
        assertThat(lastCommits.get(0)).isEqualToComparingFieldByField(commit2);
        assertThat(lastCommits.get(1)).isEqualToComparingFieldByField(commit1);
    }

    @After
    public void after() {
        repository.deleteAll();
    }

}
