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

package com.bbva.arq.devops.ae.mirrorgate.mapper;

import com.bbva.arq.devops.ae.mirrorgate.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.support.BuildStatus;

import java.util.Collections;
import java.util.List;

public class BuildMapper {

    private BuildMapper(){}

    public static Build map(BuildDTO source) {
        return map(source, new Build());
    }

    public static Build map(BuildDTO source, Build target) {

        List<String> keywords = source.getKeywords();
        if (keywords.isEmpty()) { // To keep compatibility with Mirrorgate Jenkins Plugin older than v0.0.9
            keywords.add(source.getBuildUrl());
            keywords.add(source.getProjectName());
            keywords.add(source.getRepoName());
            keywords.removeAll(Collections.singleton(null));
        }

        return target
                .setBuildUrl(source.getBuildUrl())
                .setNumber(source.getNumber())
                .setStartTime(source.getStartTime())
                .setEndTime(source.getEndTime())
                .setDuration(source.getDuration())
                .setCulprits(source.getCulprits())
                .setBuildStatus(BuildStatus.fromString(source.getBuildStatus()))
                .setTimestamp(source.getTimestamp() != null ? source.getTimestamp() : System.currentTimeMillis())
                .setProjectName(source.getProjectName())
                .setRepoName(source.getRepoName())
                .setBranch(source.getBranch())
                .setKeywords(keywords);
    }

    public static BuildDTO map(Build source) {
        return map(source, new BuildDTO());
    }

    private static BuildDTO map(Build source, BuildDTO target) {

        return target
                .setBuildUrl(source.getBuildUrl())
                .setNumber(source.getNumber())
                .setStartTime(source.getStartTime())
                .setEndTime(source.getEndTime())
                .setDuration(source.getDuration())
                .setCulprits(source.getCulprits())
                .setBuildStatus(source.getBuildStatus().toString())
                .setTimestamp(source.getTimestamp())
                .setProjectName(source.getProjectName())
                .setRepoName(source.getRepoName())
                .setBranch(source.getBranch())
            .setKeywords(source.getKeywords());
    }

}
