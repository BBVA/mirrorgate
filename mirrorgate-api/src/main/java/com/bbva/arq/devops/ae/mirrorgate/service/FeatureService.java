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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.FeatureStats;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl.ProgramIncrementNamesAggregationResult;
import java.util.List;
import java.util.regex.Pattern;
import org.bson.types.ObjectId;

/**
 * Service for Feature model
 */
public interface FeatureService {

    /**
     * Get active user stories of a project
     *
     * @param boards List of boards names
     * @return List of active Features
     */
    List<Feature> getActiveUserStoriesByBoards(List<String> boards);

    FeatureStats getFeatureStatsByKeywords(List<String> boards);

    Iterable<IssueDTO> saveOrUpdateStories(List<IssueDTO> issues, String collectorId);

    void deleteStory(Long id, String collectorId);

    List<Feature> getFeatureRelatedIssues(List<String> featuresKeys);

    List<Feature> getProductIncrementFeatures(String name);

    ProgramIncrementNamesAggregationResult getProductIncrementFromPiPattern(Pattern pi);

    List<String> getProgramIncrementFeaturesByBoard(List<String> boards, List<String> programIncrementFeatures);

    Iterable<Feature> getFeaturesByObjectId(List<ObjectId> ids);

    List<Feature> getEpicsBySNumber(List<String> keys);

}
