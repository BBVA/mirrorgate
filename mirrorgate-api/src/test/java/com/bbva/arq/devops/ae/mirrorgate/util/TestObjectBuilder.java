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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.BugDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.BugPriority;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.BugStatus;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import java.util.Arrays;
import org.bson.types.ObjectId;

/**
 *
 * @author enrique
 */
public class TestObjectBuilder {

    private static final String DASHBOARD_NAME = "mirrorgate";
    private static final String PROJECT_NAME = "mirrorgate";
    private static final String FEATURE_NAME = "feature";


    public static Dashboard createDashboard() {
        Dashboard dashboard = new Dashboard();
        dashboard.setId(ObjectId.get());
        dashboard.setName(DASHBOARD_NAME);
        dashboard.setsProductName(PROJECT_NAME);
        dashboard.setBoards(Arrays.asList(PROJECT_NAME));
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

    public static BugDTO createBugDTO() {
        BugDTO bug = new BugDTO();
        bug.setId(FEATURE_NAME);
        bug.setPriority(BugPriority.MEDIUM);
        bug.setStatus(BugStatus.IN_PROGRESS);
        return bug;
    }

}
