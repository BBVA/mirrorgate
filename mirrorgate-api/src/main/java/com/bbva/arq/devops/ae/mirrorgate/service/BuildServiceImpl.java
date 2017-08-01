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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.BuildStatus;
import com.bbva.arq.devops.ae.mirrorgate.exception.BuildConflictException;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.repository.BuildRepository;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildServiceImpl implements BuildService {

    @Autowired
    private BuildRepository buildRepository;

    @Override
    public List<Build> getAllBranchesLastByReposName(List<String> repos) {
        return buildRepository.findAllBranchesLastByReposName(repos);
    }

    @Override
    public String createOrUpdate(BuildDTO request) {
        Build build = buildRepository.save(getBuildToSave(request));
        if (build == null) {
            throw new BuildConflictException("Failed inserting/updating build information.");
        }

        return build.getId().toString();
    }

    private Build getBuildToSave(BuildDTO request) {
        Build build = null;
        if(BuildStatus.fromString(request.getBuildStatus()) != BuildStatus.Deleted && request.getBuildUrl() != null) {
            build = buildRepository.findByBuildUrl(request.getBuildUrl());
        }
        if(build == null) {
            build = new Build();
        }

        build.setBuildUrl(request.getBuildUrl());
        build.setNumber(request.getNumber());
        build.setStartTime(request.getStartTime());
        build.setEndTime(request.getEndTime());
        build.setDuration(request.getDuration());
        build.setCulprits(request.getCulprits());
        build.setBuildStatus(BuildStatus.fromString(request.getBuildStatus()));
        build.setTimestamp(System.currentTimeMillis());
        build.setProjectName(request.getProjectName());
        build.setRepoName(request.getRepoName());
        build.setBranch(request.getBranch());

        return build;
    }

    @Override
    public Map<BuildStatus, BuildStats> getBuildStatusStatsAfterTimestamp(List<String> repoName, long timestamp) {
        return buildRepository.getBuildStatusStatsAfterTimestamp(repoName, timestamp);
    }

}
