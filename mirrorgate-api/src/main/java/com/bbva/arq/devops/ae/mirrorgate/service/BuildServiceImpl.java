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
import com.bbva.arq.devops.ae.mirrorgate.exception.BuildConflictException;
import com.bbva.arq.devops.ae.mirrorgate.mapper.BuildMapper;
import com.bbva.arq.devops.ae.mirrorgate.mapper.BuildSummaryMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.BuildSummary;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.repository.BuildRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.BuildSummaryRepository;
import com.bbva.arq.devops.ae.mirrorgate.utils.BuildStatsUtils;
import com.bbva.arq.devops.ae.mirrorgate.support.BuildStatus;
import com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildServiceImpl implements BuildService {

    private static final long DAY_IN_MS = (long) 1000 * 60 * 60 * 24;

    private final BuildRepository buildRepository;
    private final BuildSummaryRepository buildSummaryRepository;
    private final EventService eventService;
    private final DashboardService dashboardService;

    @Autowired
    public BuildServiceImpl(BuildRepository buildRepository, BuildSummaryRepository buildSummaryRepository, EventService eventService, DashboardService dashboardService) {
        this.buildRepository = buildRepository;
        this.buildSummaryRepository = buildSummaryRepository;
        this.eventService = eventService;
        this.dashboardService = dashboardService;
    }

    @PostConstruct
    public void initIt() throws Exception {
        updateBuildSummaries();
    }

    @Override
    public List<Build> getLastBuildsByKeywordsAndByTeamMembers(List<String> keywords, List<String> teamMembers) {
        return buildRepository.findLastBuildsByKeywordsAndByTeamMembers(keywords, teamMembers);
    }

    @Override
    public BuildDTO createOrUpdate(BuildDTO request) {
        Build toSave = getBuildToSave(request);

        boolean shouldUpdateLatest = shouldUpdateLatest(toSave);

        if(shouldUpdateLatest) {
            toSave.setLatest(true);
        }

        Build build = buildRepository.save(toSave);

        if (build == null) {
            throw new BuildConflictException("Failed inserting/updating build "
                    + "information.");
        }

        eventService.saveEvent(build, EventType.BUILD);

        dashboardService.createDashboardForBuildProject(build);

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
                        .map( b -> b.setLatest(false))
                        .filter( b -> !b.getId().equals(toSave.getId()))
                        .collect(Collectors.toList())
                );
            }

            updateStats(request);
        }

        return map(build);
    }

    @Override
    public BuildStats getStatsByKeywordsAndByTeamMembers(List<String> keywords, List<String> teamMembers) {
        BuildStats statsSevenDaysBefore;
        BuildStats statsFifteenDaysBefore;

        if (teamMembers != null && !teamMembers.isEmpty()) {
            statsSevenDaysBefore = getStatsWithoutFailureTendency(keywords, teamMembers, 7);
            statsFifteenDaysBefore = getStatsWithoutFailureTendency(keywords, teamMembers, 15);
        } else {
            statsSevenDaysBefore = BuildStatsUtils.combineBuildStats(buildSummaryRepository
                    .findAllWithKeywordsAndTimestampAfter(keywords, LocalDateTimeHelper.getTimestampForNUnitsAgo(7, ChronoUnit.DAYS))
                    .stream()
                    .map(BuildSummaryMapper::map)
                    .toArray(BuildStats[]::new)
            );
            statsFifteenDaysBefore = BuildStatsUtils.combineBuildStats(buildSummaryRepository
                    .findAllWithKeywordsAndTimestampAfter(keywords, LocalDateTimeHelper.getTimestampForNUnitsAgo(15, ChronoUnit.DAYS))
                    .stream()
                    .map(BuildSummaryMapper::map)
                    .toArray(BuildStats[]::new)
            );
        }

        FailureTendency failureTendency = BuildStatsUtils.failureTendency(
                statsSevenDaysBefore.getFailureRate(),
                statsFifteenDaysBefore.getFailureRate());

        statsSevenDaysBefore.setFailureTendency(failureTendency);

        return statsSevenDaysBefore;
    }

    @Override
    public Map<BuildStatus, BuildStats> getBuildStatusStatsAfterTimestamp(List<String> repos, List<String> teamMembers, long timestamp) {
        return buildRepository.getBuildStatusStatsAfterTimestamp(repos, teamMembers, timestamp);
    }

    private BuildStats getStatsWithoutFailureTendency(List<String> keywords, List<String> teamMembers, int daysBefore) {

        if (keywords == null) {
            return null;
        }

        Date numberOfDaysBefore
                = new Date(System.currentTimeMillis() - (daysBefore * DAY_IN_MS));
        Map<BuildStatus, BuildStats> info = getBuildStatusStatsAfterTimestamp(
                keywords, teamMembers, numberOfDaysBefore.getTime());

        return BuildStatsUtils.combineBuildStats(
                info.values().toArray(new BuildStats[info.values().size()]));
    }


    private Build getBuildToSave(BuildDTO request) {
        Build build = null;
        if(BuildStatus.fromString(request.getBuildStatus()) != BuildStatus.Deleted && request.getBuildUrl() != null) {
            build = buildRepository.findByBuildUrl(request.getBuildUrl());
        }
        if(build == null) {
            build = new Build();
        }
        return map(request, build);
    }

    private void updateStats(BuildDTO request) {

        if (BuildStatus.fromString(request.getBuildStatus()).equals(BuildStatus.Deleted)
                || (request.getTimestamp() == null && request.getEndTime() == null)) {
            return;
        }

        BuildSummary buildSummary = buildSummaryRepository.findByRepoNameAndProjectNameAndTimestamp(request.getRepoName(), request.getProjectName(), LocalDateTimeHelper.getTimestampPeriod(request.getTimestamp() != null ? request.getTimestamp() : request.getEndTime(), ChronoUnit.DAYS));

        if (buildSummary == null) {

            buildSummary = new BuildSummary()
                    .setTimestamp(LocalDateTimeHelper.getTimestampPeriod(request.getTimestamp() != null ? request.getTimestamp() : request.getEndTime(), ChronoUnit.DAYS))
                    .setProjectName(request.getProjectName())
                    .setRepoName(request.getRepoName())
                    .setStatusMap(new HashMap<>())
                    .setTotalBuilds(0L)
                    .setTotalDuration(0.0);
        }

        buildSummary
                .setTotalBuilds(buildSummary.getTotalBuilds() + 1)
                .setTotalDuration(buildSummary.getTotalDuration() + request.getDuration());
        Long nBuildStatus = buildSummary.getStatusMap().get(BuildStatus.fromString(request.getBuildStatus()));
        buildSummary.getStatusMap().put(BuildStatus.fromString(request.getBuildStatus()), nBuildStatus != null ? nBuildStatus + 1 : 1L);

        buildSummaryRepository.save(buildSummary);
    }

    private void updateBuildSummaries() {
        if (buildSummaryRepository.count() == 0 && buildRepository.count() > 0) {
            buildRepository.findAllByTimestampAfter(LocalDateTimeHelper.getTimestampForOneMonthAgo())
                    .stream()
                    .filter((build) -> this.shouldUpdateLatest(build))
                    .map(BuildMapper::map)
                    .forEach(this::updateStats);
        }

    }

    private boolean shouldUpdateLatest(Build build) {
        return build.getBuildStatus() != BuildStatus.Aborted
                && build.getBuildStatus() != BuildStatus.Unknown
                && build.getBuildStatus() != BuildStatus.InProgress
                && build.getBuildStatus() != BuildStatus.NotBuilt;
    }
}
