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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.BuildStatus;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;

import java.util.ArrayList;

/**
 * Created by alfonso on 28/05/17.
 */
public class BuildMapper {

    private BuildMapper(){}

    public static Build map(BuildDTO source) {
        return map(source, new Build());
    }

    public static Build map(BuildDTO source, Build target) {
        target.setBuildUrl(source.getBuildUrl());
        target.setNumber(source.getNumber());
        target.setStartTime(source.getStartTime());
        target.setEndTime(source.getEndTime());
        target.setDuration(source.getDuration());
        target.setCulprits(source.getCulprits());
        target.setBuildStatus(BuildStatus.fromString(source.getBuildStatus()));
        if(source.getTimestamp() != null) {
            target.setTimestamp(source.getTimestamp());
        } else {
            target.setTimestamp(System.currentTimeMillis());
        }

        target.setProjectName(source.getProjectName());
        target.setRepoName(source.getRepoName());
        target.setBranch(source.getBranch());

        ArrayList<String> keywords = new ArrayList<>();
        keywords.add(source.getBuildUrl());
        keywords.add(source.getProjectName());
        keywords.add(source.getRepoName());
        target.setKeywords(keywords);

        return target;
    }

    public static BuildDTO map(Build source) {
        return map(source, new BuildDTO());
    }

    public static BuildDTO map(Build source, BuildDTO target) {
        target.setBuildUrl(source.getBuildUrl());
        target.setNumber(source.getNumber());
        target.setStartTime(source.getStartTime());
        target.setEndTime(source.getEndTime());
        target.setDuration(source.getDuration());
        target.setCulprits(source.getCulprits());
        target.setBuildStatus(source.getBuildStatus().toString());
        target.setTimestamp(source.getTimestamp());

        target.setProjectName(source.getProjectName());
        target.setRepoName(source.getRepoName());
        target.setBranch(source.getBranch());

        ArrayList<String> keywords = new ArrayList<>();
        keywords.add(source.getBuildUrl());
        keywords.add(source.getProjectName());
        keywords.add(source.getRepoName());
        target.setKeywords(keywords);
        return target;
    }


}
