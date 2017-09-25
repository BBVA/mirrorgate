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
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import java.util.List;
import java.util.Map;

/**
 * Continuous Integration build service.
 */
public interface BuildService {

    /**
     * Get last builds by repositories and filtered by team members if those
     * exist.
     *
     * @param repos
     * @param teamMembers
     * @return List of builds
     */
    List<Build> getLastBuildsByReposNameAndByTeamMembers(
            List<String> repos, List<String> teamMembers);

    /**
     * Create a build from a request
     *
     * @param request Build request type
     * @return Id of the new Build
     */
    String createOrUpdate(BuildDTO request);

    /**
     * Get a list of builds by repositories with timestamp after specified and
     * filtered by team members if those exist.
     *
     * @param repos
     * @param teamMembers
     * @param timestamp
     * @return
     */
    Map<BuildStatus, BuildStats> getBuildStatusStatsAfterTimestamp(
            List<String> repos, List<String> teamMembers, long timestamp);

    /**
     * Get statistics of builds from a repositories and filtered by team members
     * if those exist.
     *
     * @param repos
     * @param teamMembers
     * @return
     */
    BuildStats getStatsFromReposByTeamMembers(
            List<String> repos, List<String> teamMembers);

}
