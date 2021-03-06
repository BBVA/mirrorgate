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

import static com.bbva.arq.devops.ae.mirrorgate.builders.BuildBuilder.makeBuild;
import static org.assertj.core.api.Assertions.assertThat;

import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataMongoTest
public class BuildRepositoryTests {

    @Autowired
    private BuildRepository repository;

    private static final String REPO_NAME = "mirrorgate";
    private static final String[] CULPRITS = {"Atreyu", "Gmork", "Xayide"};

    @Test
    public void getLastByRepoNameAndByTeamMembers() {
        final List<String> repos = Collections.singletonList(REPO_NAME);
        final List<String> teamMembers = Arrays.asList(CULPRITS[1], CULPRITS[2]);

        final Build build1 = makeBuild(REPO_NAME, "develop", Collections.singletonList(CULPRITS[0]))
            .setBuildUrl("Build1");
        final Build build2 = makeBuild(REPO_NAME, "develop", Collections.singletonList(CULPRITS[2]))
            .setBuildUrl("Build1");
        final Build build3 = makeBuild(REPO_NAME, "master", Collections.singletonList(CULPRITS[2]))
            .setBuildUrl("Build2");
        final Build build4 = makeBuild(REPO_NAME, "master", Collections.singletonList(CULPRITS[1]))
            .setBuildUrl("Build2");

        repository.save(build1);
        repository.save(build2);
        repository.save(build3);
        repository.save(build4);

        final List<Build> builds = repository.findLastBuildsByKeywordsAndByTeamMembers(repos, teamMembers);

        assertThat(builds.size()).isEqualTo(2);
        assertThat(builds.get(0).getTimestamp()).isEqualTo(build4.getTimestamp());
        assertThat(builds.get(0).getBranch()).isEqualTo(build4.getBranch());
        assertThat(builds.get(1).getTimestamp()).isEqualTo(build2.getTimestamp());
        assertThat(builds.get(1).getBranch()).isEqualTo(build2.getBranch());
    }

    @Test
    public void getLastByRepoNameAndByTeamMembersWithoutTeamMembers() {
        final List<String> repos = Collections.singletonList(REPO_NAME);

        final Build build1 = makeBuild(REPO_NAME, "develop");
        final Build build2 = makeBuild(REPO_NAME, "master");
        final Build build3 = makeBuild(REPO_NAME, "master");

        repository.save(build1);
        repository.save(build2);
        repository.save(build3);

        final List<Build> builds = repository.findLastBuildsByKeywordsAndByTeamMembers(repos, null);

        assertThat(builds.get(0).getTimestamp()).isEqualTo(build3.getTimestamp());
        assertThat(builds.get(0).getBranch()).isEqualTo(build3.getBranch());
        assertThat(builds.get(1).getTimestamp()).isEqualTo(build1.getTimestamp());
        assertThat(builds.get(1).getBranch()).isEqualTo(build1.getBranch());
    }

    @Test
    public void getBuildsByIds() {
        final Build build1 = makeBuild(REPO_NAME, "develop");
        final Build build2 = makeBuild(REPO_NAME, "master");
        final Build build3 = makeBuild(REPO_NAME, "master");

        repository.save(build1);
        repository.save(build2);
        repository.save(build3);

        final List<Build> builds = repository
            .findAllByIdIn(Arrays.asList(build1.getId().toString(), build2.getId().toString()));

        assertThat(builds.get(0).getId()).isEqualTo(build1.getId());
        assertThat(builds.get(0).getTimestamp()).isEqualTo(build1.getTimestamp());
        assertThat(builds.get(0).getBranch()).isEqualTo(build1.getBranch());
        assertThat(builds.get(1).getId()).isEqualTo(build2.getId());
        assertThat(builds.get(1).getTimestamp()).isEqualTo(build2.getTimestamp());
        assertThat(builds.get(1).getBranch()).isEqualTo(build2.getBranch());
    }


    @After
    public void after() {
        repository.deleteAll();
    }
}