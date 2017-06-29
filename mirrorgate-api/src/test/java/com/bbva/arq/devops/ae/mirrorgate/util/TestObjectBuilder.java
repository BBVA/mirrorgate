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
package com.bbva.arq.devops.ae.mirrorgate.util;

import static com.bbva.arq.devops.ae.mirrorgate.core.utils.IssueType.BUG;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationReviewsDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.BugDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.SlackDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.BugPriority;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.BugStatus;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author enrique
 */
public class TestObjectBuilder {

    private static final String DASHBOARD_NAME = "mirrorgate";
    private static final String PROJECT_NAME = "mirrorgate";
    private static final String FEATURE_NAME = "feature";
    public static final String AUTH_NAME = "ATREYU";


    private static final String SLACK_TEAM = "SLACK_TEAM";
    private static final String SLACK_TOKEN = "SLACK_TOKEN";
    private static final String SLACK_URL = "SLACK_URL";
    private static final String SLACK_ERROR = "SLACK_ERROR";

    public static Dashboard createDashboard() {
        Dashboard dashboard = new Dashboard();
        dashboard.setId(ObjectId.get());
        dashboard.setName(DASHBOARD_NAME);
        dashboard.setAuthor(AUTH_NAME);
        String urlRepo1 = "http.//repo1.git";
        String urlRepo2 = "http.//repo2.git";
        List<String> codeRepos = new ArrayList<>();
        codeRepos.add(urlRepo1);
        codeRepos.add(urlRepo2);
        dashboard.setCodeRepos(codeRepos);
        dashboard.setsProductName(PROJECT_NAME);
        dashboard.setBoards(Arrays.asList(PROJECT_NAME));
        dashboard.setSlackTeam(SLACK_TEAM);
        dashboard.setSlackToken(SLACK_TOKEN);
        return dashboard;
    }

    public static Feature createBug() {
        Feature bug = createActiveStory();
        bug.setsTypeName(BUG.getName());
        return bug;
    }

    public static Feature createActiveStory() {
        Feature story = new Feature();
        story.setId(ObjectId.get());
        story.setsId(ObjectId.get().toString());
        story.setsSprintAssetState("Active");
        story.setsProjectName(PROJECT_NAME);
        story.setsNumber(FEATURE_NAME);
        return story;
    }

    public static SlackDTO createSlackDTO() {
        SlackDTO notification = new SlackDTO();
        notification.setOk(true);
        notification.setAccess_token(SLACK_TOKEN);
        notification.setUrl(SLACK_URL);
        return notification;
    }

    public static SlackDTO createSlackErrorDTO() {
        SlackDTO notification = new SlackDTO();
        notification.setOk(false);
        notification.setError(SLACK_ERROR);
        return notification;
    }

    public static BugDTO createBugDTO() {
        BugDTO bug = new BugDTO();
        bug.setId(FEATURE_NAME);
        bug.setPriority(BugPriority.MEDIUM);
        bug.setStatus(BugStatus.IN_PROGRESS);
        return bug;
    }

    public static DashboardDTO createDashboardDTO() {
        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setName(DASHBOARD_NAME);
        String urlRepo1 = "http.//repo1.git";
        String urlRepo2 = "http.//repo2.git";
        List<String> codeRepos = new ArrayList<>();
        codeRepos.add(urlRepo1);
        codeRepos.add(urlRepo2);
        dashboard.setCodeRepos(codeRepos);
        dashboard.setsProductName(PROJECT_NAME);
        dashboard.setBoards(Arrays.asList(PROJECT_NAME));
        return dashboard;
    }

    public static DashboardDTO createDashboardDTO(String name, List<String> applications) {
        DashboardDTO dashboard = createDashboardDTO();

        dashboard.setName(name);
        dashboard.setApplications(applications);
        return dashboard;
    }

    public static ApplicationReviewsDTO createApplicationDTO(String name, Platform platform) {

        ApplicationReviewsDTO applicationReviewsDTO = new ApplicationReviewsDTO();

        applicationReviewsDTO.setCommentId("12");
        applicationReviewsDTO.setAppId(name);
        applicationReviewsDTO.setAppName(name);
        applicationReviewsDTO.setPlatform(platform);

        return applicationReviewsDTO;
    }
}
