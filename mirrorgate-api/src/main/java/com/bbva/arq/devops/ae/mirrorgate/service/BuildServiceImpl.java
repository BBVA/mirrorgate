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
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.repository.BuildRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildServiceImpl implements BuildService {

    private BuildRepository buildRepository;
    private EventService eventService;

    @Autowired
    public BuildServiceImpl(BuildRepository buildRepository, EventService eventService) {

        this.buildRepository = buildRepository;
        this.eventService = eventService;
    }

    @Override
    public List<Build> getAllBranchesLastByReposName(List<String> repos) {
        return buildRepository.findAllBranchesLastByReposName(repos);
    }

    @Override
    public String createOrUpdate(BuildDTO request) {
        Build toSave = getBuildToSave(request);

        boolean shouldUpdateLatest = toSave.getBuildStatus() != BuildStatus.Aborted &&
                toSave.getBuildStatus() != BuildStatus.Unknown &&
                toSave.getBuildStatus() != BuildStatus.InProgress &&
                toSave.getBuildStatus() != BuildStatus.NotBuilt;

        if(shouldUpdateLatest) {
            toSave.setLatest(true);
        }

        Build build = buildRepository.save(toSave);

        if (build == null) {
            throw new BuildConflictException("Failed inserting/updating build information.");
        }

        eventService.saveBuildEvent(build);

        if(shouldUpdateLatest) {
            List<Build> toUpdate =
                    buildRepository.findAllByRepoNameAndProjectNameAndBranchAndLatestIsTrue(
                            toSave.getRepoName(),
                            toSave.getProjectName(),
                            toSave.getBranch()
                    );

            if(toUpdate != null){
                buildRepository.save(
                        toUpdate.stream()
                        .map((b) -> b.setLatest(false))
                        .filter((b) -> !b.getId().equals(toSave.getId()))
                        .collect(Collectors.toList())
                );
            }

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
        build.setStartedBy(request.getStartedBy());
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

    public List<Build> getAllBuildsFromId(List<ObjectId> buildIds){
        return buildRepository.findByIdIn(buildIds);
    }

}
