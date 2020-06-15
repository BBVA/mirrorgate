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

import com.bbva.arq.devops.ae.mirrorgate.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.IssueStats;
import com.bbva.arq.devops.ae.mirrorgate.exception.IssueNotFoundException;
import com.bbva.arq.devops.ae.mirrorgate.mapper.IssueMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.repository.IssueRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.IssueRepositoryImpl.ProgramIncrementNamesAggregationResult;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueType;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository repository;
    private final DashboardService dashboardService;
    private final EventService eventService;

    @Autowired
    public IssueServiceImpl(
        final IssueRepository repository,
        final DashboardService dashboardService,
        final EventService eventService
    ) {
        this.repository = repository;
        this.dashboardService = dashboardService;
        this.eventService = eventService;
    }


    @Override
    public List<Issue> getActiveUserStoriesByBoards(final List<String> boards) {
        return repository.findActiveUserStoriesByBoards(boards, Sort.by(Order.by("status")));
    }

    @Override
    public List<Issue> getFeatureRelatedIssues(final List<String> featuresKeys) {
        return repository.findAllByParentsKeysIn(featuresKeys);
    }

    @Override
    public List<Issue> getProductIncrementFeatures(final String name) {
        return repository.findAllByTypeAndPiNames(IssueType.FEATURE.getName(), name);
    }

    @Override
    public ProgramIncrementNamesAggregationResult getProductIncrementFromPiPattern(final Pattern pi) {
        return repository.getProductIncrementFromPiPattern(pi);
    }

    @Override
    public List<String> getProgramIncrementFeaturesByBoard(
        final List<String> boards,
        final List<String> programIncrementFeatures
    ) {
        return repository.programIncrementBoardFeatures(boards, programIncrementFeatures);
    }

    @Override
    public IssueStats getIssueStatsByKeywords(final List<String> boards) {
        final IssueStats result = new IssueStats();

        result.setBacklogEstimate(repository.getBacklogEstimateByKeywords(boards));
        result.setSprintStats(repository.getSprintStatsByKeywords(boards));
        return result;
    }

    @Override
    public Iterable<IssueDTO> saveOrUpdateStories(
        final List<IssueDTO> issuesDTO,
        final String collectorId
    ) {

        final List<Issue> issues = issuesDTO.stream()
            .map(issue -> IssueMapper.map(issue).setCollectorId(collectorId))
            .collect(Collectors.toList());

        createTransientDashboardsForTeams(issues);

        return StreamSupport.stream(repository.saveAll(issues).spliterator(), false)
            .map((feat) -> {
                eventService.saveEvent(feat, EventType.ISSUE);
                    return new IssueDTO()
                        .setId(Long.parseLong(feat.getIssueId()))
                        .setName(feat.getName())
                        .setEstimate(feat.getEstimation());
                }
            )
            .collect(Collectors.toList());
    }

    @Override
    public IssueDTO deleteStory(final String id, final String collectorId) {
        final Issue issue = repository.findFirstByIssueIdAndCollectorId(id, collectorId);

        if (issue == null) {
            throw new IssueNotFoundException(MessageFormat.format("Story with id {0} not found", id));
        }

        repository.deleteByIssueIdAndCollectorId(id, collectorId);
        eventService.saveEvent(new Issue(), EventType.ISSUE);
        return new IssueDTO()
            .setId(Long.parseLong(issue.getIssueId()))
            .setName(issue.getName())
            .setEstimate(issue.getEstimation());
    }

    @Override
    public Iterable<Issue> getIssuesById(final List<String> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public List<Issue> getEpicsByNumber(final List<String> keys) {
        return repository.findAllByNumberInAndType(keys, IssueType.EPIC.getName());
    }

    private void createTransientDashboardsForTeams(final List<Issue> issues) {
        issues.stream()
            .map(Issue::getTeamName)
            .filter(teamName -> teamName != null && !teamName.isEmpty())
            .distinct()
            .forEach(dashboardService::createDashboardForJiraTeam);
    }
}