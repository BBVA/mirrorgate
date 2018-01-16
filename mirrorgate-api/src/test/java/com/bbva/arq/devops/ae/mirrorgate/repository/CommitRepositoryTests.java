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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CommitRepositoryTests {

    private static final String[] GIT_REPO_URLS = new String[]{
        "ssh://repo1.git",
        "ssh://repo2.git"
    };

    @Autowired
    private CommitRepository repository;

    @Before
    public void before() {
    }

    @Test
    public void getSecondsToMasterByRepoListTest() {
        List<String> repos = Arrays.asList(GIT_REPO_URLS);
        int oneDayAgo = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (24 * 60 * 60);
        int timestamp1 = oneDayAgo - (60 * 60);
        int timestamp2 = oneDayAgo - (2 * 60 * 60);

        HashMap<String, Integer> branches1 = new HashMap<String, Integer>(){
            {
                put("refs/remotes/origin/master", oneDayAgo);
            }
        };

        HashMap<String, Integer> branches2 = new HashMap<String, Integer>(){
            {
                put("refs/remotes/origin/master", oneDayAgo);
            }
        };

        Commit commit1 = new Commit().setRepository(repos.get(0)).setTimestamp(timestamp1).setBranches(branches1);
        Commit commit2 = new Commit().setRepository(repos.get(1)).setTimestamp(timestamp2).setBranches(branches2);

        repository.save(commit1);
        repository.save(commit2);

        Double seconds1 = repository.getSecondsToMaster(Arrays.asList(repos.get(0)), 30);
        Double seconds2 = repository.getSecondsToMaster(repos, 30);

        assertThat(seconds1).isEqualTo(oneDayAgo - timestamp1);
        assertThat(seconds2).isEqualTo(((oneDayAgo - timestamp1) + (oneDayAgo - timestamp2)) / 2);
    }

    @Test
    public void getCommitsPerDayByRepoListTest() {
        List<String> repos = Arrays.asList(GIT_REPO_URLS);
        int oneDayAgo = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (24 * 60 * 60);
        int sevenDaysAgo = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - (7 * 24 * 60 * 60);

        Commit commit1 = new Commit().setRepository(repos.get(0)).setTimestamp(oneDayAgo);
        Commit commit2 = new Commit().setRepository(repos.get(0)).setTimestamp(oneDayAgo);
        Commit commit3 = new Commit().setRepository(repos.get(1)).setTimestamp(oneDayAgo);
        Commit commit4 = new Commit().setRepository(repos.get(1)).setTimestamp(oneDayAgo);
        Commit commit5 = new Commit().setRepository(repos.get(1)).setTimestamp(oneDayAgo);
        Commit commit6 = new Commit().setRepository(repos.get(1)).setTimestamp(sevenDaysAgo);

        repository.save(commit1);
        repository.save(commit2);
        repository.save(commit3);
        repository.save(commit4);
        repository.save(commit5);
        repository.save(commit6);

        Double commits1 = repository.getCommitsPerDay(Arrays.asList(repos.get(0)), 30);
        Double commits2 = repository.getCommitsPerDay(Arrays.asList(repos.get(1)), 30);
        Double commits3 = repository.getCommitsPerDay(repos, 30);
        Double commits4 = repository.getCommitsPerDay(repos, 3);

        assertThat(commits1).isEqualTo(2D / 30D);
        assertThat(commits2).isEqualTo(4D / 30D);
        assertThat(commits3).isEqualTo(6D / 30D);
        assertThat(commits4).isEqualTo(5D / 3D);
    }

    @After
    public void after() {
        repository.deleteAll();
    }

}
