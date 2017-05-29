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
package com.bbva.arq.devops.ae.mirrorgate.builders;

import com.bbva.arq.devops.ae.mirrorgate.core.utils.BuildStatus;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import org.bson.types.ObjectId;

public class BuildBuilder {
    private BuildBuilder() {}

    public static Build makeBuild() {
        return makeBuild("http://repo1.com/");
    }

    public static Build makeBuild(String repoName, String branchName) {
        Build build = makeBuild(repoName);
        build.setBranch(branchName);
        return build;
    }

    public static Build makeBuild(String repoName) {
        return makeBuild(repoName, BuildStatus.Success);
    }

    public static Build makeBuild(String repoName, BuildStatus buildStatus) {
        Build build = new Build();
        build.setId(ObjectId.get());
        build.setTimestamp(1);
        build.setNumber("1");
        build.setBuildUrl("buildUrl");
        build.setStartTime(3);
        build.setEndTime(8);
        build.setDuration(5);
        build.setBuildStatus(buildStatus);
        build.setStartedBy("foo");
        build.setProjectName("mirrorgate");
        build.setRepoName(repoName);
        build.setBranch("test");
        return build;
    }

}
