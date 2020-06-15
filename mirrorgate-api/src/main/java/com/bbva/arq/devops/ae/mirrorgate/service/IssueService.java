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
import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.repository.IssueRepositoryImpl.ProgramIncrementNamesAggregationResult;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Service for Issue model.
 */
public interface IssueService {

    /**
     * Get active user stories of a project.
     *
     * @param boards List of boards names
     * @return List of active Issues
     */
    List<Issue> getActiveUserStoriesByBoards(List<String> boards);

    IssueStats getIssueStatsByKeywords(List<String> boards);

    Iterable<IssueDTO> saveOrUpdateStories(List<IssueDTO> issues, String collectorId);

    IssueDTO deleteStory(String id, String collectorId);

    List<Issue> getFeatureRelatedIssues(List<String> featuresKeys);

    List<Issue> getProductIncrementFeatures(String name);

    ProgramIncrementNamesAggregationResult getProductIncrementFromPiPattern(Pattern pi);

    List<String> getProgramIncrementFeaturesByBoard(List<String> boards, List<String> programIncrementFeatures);

    Iterable<Issue> getIssuesById(List<String> ids);

    List<Issue> getEpicsByNumber(List<String> keys);

}
