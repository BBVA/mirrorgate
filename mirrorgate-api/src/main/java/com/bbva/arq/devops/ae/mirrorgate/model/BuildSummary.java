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
package com.bbva.arq.devops.ae.mirrorgate.model;

import com.bbva.arq.devops.ae.mirrorgate.core.utils.BuildStatus;
import java.util.Map;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "builds_summary")
public class BuildSummary extends BaseModel {

    @Indexed
    private Long timestamp;
    @Indexed
    private String projectName;
    @Indexed
    private String repoName;
    private Map<BuildStatus, Long> statusMap;
    private Long totalBuilds;
    private Double totalDuration;

    public Long getTimestamp() {
        return timestamp;
    }

    public BuildSummary setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public BuildSummary setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getRepoName() {
        return repoName;
    }

    public BuildSummary setRepoName(String repoName) {
        this.repoName = repoName;
        return this;
    }

    public Map<BuildStatus, Long> getStatusMap() {
        return statusMap;
    }

    public BuildSummary setStatusMap(Map<BuildStatus, Long> statusMap) {
        this.statusMap = statusMap;
        return this;
    }

    public Long getTotalBuilds() {
        return totalBuilds;
    }

    public BuildSummary setTotalBuilds(Long totalBuilds) {
        this.totalBuilds = totalBuilds;
        return this;
    }

    public Double getTotalDuration() {
        return totalDuration;
    }

    public BuildSummary setTotalDuration(Double totalDuration) {
        this.totalDuration = totalDuration;
        return this;
    }
}
