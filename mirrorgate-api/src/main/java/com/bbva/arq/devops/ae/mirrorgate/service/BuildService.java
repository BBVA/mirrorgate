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

import com.bbva.arq.devops.ae.mirrorgate.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.support.BuildStatus;
import java.util.List;
import java.util.Map;

/**
 * Continuous Integration build service.
 */
public interface BuildService {

    /**
     * Get last builds by keywords (repositories names, projects names or builds urls)
     * and filtered by team members if those exist.
     *
     * @param keywords
     * @param teamMembers
     * @return List of builds
     */
    List<Build> getLastBuildsByKeywordsAndByTeamMembers(List<String> keywords, List<String> teamMembers);

    /**
     * Create a build from a request
     *
     * @param request Build request type
     * @return Id of the new Build
     */
    BuildDTO createOrUpdate(BuildDTO request);

    /**
     * Get a list of builds by repositories with timestamp after specified and
     * filtered by team members if those exist.
     *
     * @param repos
     * @param teamMembers
     * @param timestamp
     * @return
     */
    Map<BuildStatus, BuildStats> getBuildStatusStatsAfterTimestamp(List<String> repos, List<String> teamMembers, long timestamp);

    /**
     * Get statistics of builds by keywords (repositories names, projects names or builds urls)
     * and filtered by team members if those exist.
     *
     * @param keywords
     * @param teamMembers
     * @return
     */
    BuildStats getStatsByKeywordsAndByTeamMembers(List<String> keywords, List<String> teamMembers);

}
