/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bbva.arq.devops.ae.mirrorgate.support;

import com.bbva.arq.devops.ae.mirrorgate.dto.*;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.bbva.arq.devops.ae.mirrorgate.support.IssueType.BUG;

public class TestObjectFactory {

    private static final String DASHBOARD_NAME = "mirrorgate";
    private static final String PROJECT_NAME = "mirrorgate";
    private static final String FEATURE_NAME = "feature";
    public static final String AUTH_NAME = "ATREYU";

    private static final String SLACK_TEAM = "SLACK_TEAM";
    private static final String SLACK_TOKEN = "SLACK_TOKEN";
    private static final String SLACK_URL = "SLACK_URL";
    private static final String SLACK_ERROR = "SLACK_ERROR";

    public static DashboardDTO createDashboard() {
        String urlRepo1 = "http.//repo1.git";
        String urlRepo2 = "http.//repo2.git";
        List<String> codeRepos = new ArrayList<>();
        codeRepos.add(urlRepo1);
        codeRepos.add(urlRepo2);

        return new DashboardDTO()
            .setName(DASHBOARD_NAME)
            .setAuthor(AUTH_NAME)
            .setCodeRepos(codeRepos)
            .setsProductName(PROJECT_NAME)
            .setBoards(Collections.singletonList(PROJECT_NAME))
            .setSlackTeam(SLACK_TEAM)
            .setSlackToken(SLACK_TOKEN);
    }

    public static DashboardDTO createTransientDashboard() {
        return createDashboard().setStatus(DashboardStatus.TRANSIENT);
    }

    public static Feature createBug() {
        return createActiveStory()
            .setsTypeName(BUG.getName());
    }

    public static Feature createActiveStory() {
        Feature story = new Feature();
        story.setId(ObjectId.get());
        return story.setsId(ObjectId.get().toString())
            .setsSprintAssetState("Active")
            .setsProjectName(PROJECT_NAME)
            .setsNumber(FEATURE_NAME);
    }

    public static SlackDTO createSlackDTO() {
        return new SlackDTO()
            .setOk(true)
            .setAccess_token(SLACK_TOKEN)
            .setUrl(SLACK_URL);
    }

    public static SlackDTO createSlackErrorDTO() {
        return new SlackDTO()
            .setOk(false)
            .setError(SLACK_ERROR);
    }

    public static BugDTO createBugDTO() {
        return new BugDTO()
            .setId(FEATURE_NAME)
            .setPriority(BugPriority.MEDIUM)
            .setStatus(BugStatus.IN_PROGRESS);
    }

    public static DashboardDTO createDashboardDTO() {
        String urlRepo1 = "http.//repo1.git";
        String urlRepo2 = "http.//repo2.git";
        List<String> codeRepos = new ArrayList<>();
        codeRepos.add(urlRepo1);
        codeRepos.add(urlRepo2);

        return new DashboardDTO()
            .setName(DASHBOARD_NAME)
            .setCodeRepos(codeRepos)
            .setsProductName(PROJECT_NAME)
            .setBoards(Collections.singletonList(PROJECT_NAME));
    }

    public static DashboardDTO createDashboardDTO(String name, List<String> applications) {
        return createDashboardDTO()
            .setName(name)
            .setApplications(applications);
    }

    public static ApplicationReviewsDTO createApplicationDTO(String name, Platform platform) {
        return new ApplicationReviewsDTO()
            .setCommentId("12")
            .setAppId(name)
            .setAppName(name)
            .setPlatform(platform);
    }

    public static Review createReview(Platform platform, String appName, String commentId, String comment, long timestamp, double rate, int amount) {
        return new Review()
            .setTimestamp(timestamp)
            .setAppname(appName)
            .setComment(comment)
            .setPlatform(platform)
            .setCommentId(commentId)
            .setStarrating(rate)
            .setAmount(amount);
    }

    public static BuildDTO createBuildDTO() {
        return new BuildDTO()
            .setNumber("1")
            .setBuildUrl(ObjectId.get().toString()) // To ensure each time is different
            .setStartTime(3)
            .setEndTime(8)
            .setDuration(5)
            .setBuildStatus("Success")
            .setCulprits(Collections.singletonList("foo"))
            .setProjectName("mirrorgate")
            .setRepoName("develop")
            .setBranch("test")
            .setTimestamp(5L)
            .setKeywords(Arrays.asList("buildUrl", "mirrorgate", "develop"));
    }

    public static IssueDTO createIssueDTO(Long id, String name, String collectorId) {
        return createIssueDTO(id, name, collectorId, null);
    }

    public static IssueDTO createIssueDTO(Long id, String name, String collectorId, String teamName) {
        return new IssueDTO()
            .setId(id)
            .setCollectorId(collectorId)
            .setName(name)
            .setStatus(IssueStatus.DONE)
            .setTeamName(teamName);
    }

}
