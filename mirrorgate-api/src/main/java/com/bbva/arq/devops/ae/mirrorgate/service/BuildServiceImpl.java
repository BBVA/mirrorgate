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

import static com.bbva.arq.devops.ae.mirrorgate.mapper.BuildMapper.map;

import com.bbva.arq.devops.ae.mirrorgate.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.dto.FailureTendency;
import com.bbva.arq.devops.ae.mirrorgate.mapper.BuildMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.repository.BuildRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.BuildStatus;
import com.bbva.arq.devops.ae.mirrorgate.utils.BuildStatsUtils;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildServiceImpl implements BuildService {

    private static final long DAY_IN_MS = (long) 1000 * 60 * 60 * 24;

    private final BuildRepository buildRepository;
    private final EventService eventService;
    private final DashboardService dashboardService;

    @Autowired
    public BuildServiceImpl(
        final BuildRepository buildRepository,
        final EventService eventService,
        final DashboardService dashboardService
    ) {
        this.buildRepository = buildRepository;
        this.eventService = eventService;
        this.dashboardService = dashboardService;
    }

    @Override
    public List<Build> getLastBuildsByKeywordsAndByTeamMembers(
        final List<String> keywords,
        final List<String> teamMembers
    ) {
        return buildRepository.findLastBuildsByKeywordsAndByTeamMembers(keywords, teamMembers);
    }

    @Override
    public BuildDTO createOrUpdate(final BuildDTO request) {
        final Build toSave = getBuildToSave(request);

        final boolean shouldUpdateLatest = shouldUpdateLatest(toSave);

        if (shouldUpdateLatest) {
            toSave.setLatest(true);
        }

        final Build build = buildRepository.save(toSave);

        eventService.saveEvent(build, EventType.BUILD);

        dashboardService.createDashboardForBuildProject(build);

        if (shouldUpdateLatest) {
            final List<Build> toUpdate =
                buildRepository.findAllByProjectNameAndRepoNameAndBranchAndLatestIsTrue(
                    toSave.getProjectName(),
                    toSave.getRepoName(),
                    toSave.getBranch()
                );

            if (toUpdate != null) {
                buildRepository.saveAll(
                    toUpdate.stream()
                        .map(b -> b.setLatest(false))
                        .filter(b -> !b.getId().equals(toSave.getId()))
                        .collect(Collectors.toList())
                );
            }

        }

        return map(build);
    }

    @Override
    public BuildStats getStatsAndTendenciesByKeywordsAndByTeamMembers(
        final List<String> keywords,
        final List<String> teamMembers
    ) {

        final BuildStats statsSevenDaysBefore =
            getStatsByKeywordsAndByTeamMembersAfterTimestamp(keywords, teamMembers, 7);
        final BuildStats statsFifteenDaysBefore =
            getStatsByKeywordsAndByTeamMembersAfterTimestamp(keywords, teamMembers, 15);

        final FailureTendency failureTendency = BuildStatsUtils.failureTendency(
            statsSevenDaysBefore.getFailureRate(),
            statsFifteenDaysBefore.getFailureRate());

        statsSevenDaysBefore.setFailureTendency(failureTendency);

        return statsSevenDaysBefore;
    }

    @Override
    public List<BuildDTO> getBuildsByIds(final List<String> ids) {
        return buildRepository.findAllByIdIn(ids).stream().map(BuildMapper::map).collect(Collectors.toList());
    }

    private BuildStats getStatsByKeywordsAndByTeamMembersAfterTimestamp(
        final List<String> keywords,
        final List<String> teamMembers,
        final int daysBefore
    ) {
        final Date numberOfDaysBefore = new Date(System.currentTimeMillis() - (daysBefore * DAY_IN_MS));
        final Map<BuildStatus, BuildStats> info = buildRepository.getBuildStatusStatsAfterTimestamp(
            keywords, teamMembers, numberOfDaysBefore.getTime());

        return BuildStatsUtils.combineBuildStats(
            info.values().toArray(new BuildStats[0]));
    }


    private Build getBuildToSave(final BuildDTO request) {
        Build build = null;
        if (BuildStatus.fromString(request.getBuildStatus()) != BuildStatus.Deleted && request.getBuildUrl() != null) {
            build = buildRepository.findById(request.getBuildUrl());
        }
        if (build == null) {
            build = new Build();
        }
        return map(request, build);
    }

    private boolean shouldUpdateLatest(final Build build) {
        return build.getBuildStatus() != BuildStatus.Aborted
            && build.getBuildStatus() != BuildStatus.Unknown
            && build.getBuildStatus() != BuildStatus.InProgress
            && build.getBuildStatus() != BuildStatus.NotBuilt;
    }
}
