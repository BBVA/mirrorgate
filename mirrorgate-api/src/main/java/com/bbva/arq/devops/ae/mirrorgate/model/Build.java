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

import com.bbva.arq.devops.ae.mirrorgate.support.BuildStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Continuous Integration build model.
 *
 * Collectors:
 *   Jenkins
 */
@Document(collection="builds")
@CompoundIndexes({
    @CompoundIndex(name = "repositories", def = "{'projectName' : 1, 'repoName': 1, 'branch': 1, 'latest': 1}"),
    @CompoundIndex(name = "stats", def = "{'keywords': 1, 'teamMembers': 1, 'buildStatus': 1, 'timestamp': 1}")
})
public class Build implements BaseModel {

    @Id
    private String id;
    private String buildUrl;

    private long timestamp;

    private long startTime;
    private long endTime;
    private long duration;

    private BuildStatus buildStatus;
    private List<String> culprits;

    private String projectName;
    private String repoName;
    private String branch;
    private String number;

    private Boolean latest;

    private List<String> keywords;

    public long getTimestamp() {
        return timestamp;
    }

    public Build setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public Build setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getBuildUrl() {
        return buildUrl;
    }

    public Build setBuildUrl(String buildUrl) {
        this.buildUrl = buildUrl;
        this.id = buildUrl;
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    public Build setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public long getEndTime() {
        return endTime;
    }

    public Build setEndTime(long endTime) {
        this.endTime = endTime;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public Build setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public BuildStatus getBuildStatus() {
        return buildStatus;
    }

    public Build setBuildStatus(BuildStatus buildStatus) {
        this.buildStatus = buildStatus;
        return this;
    }

    public List<String> getCulprits() {
        return culprits;
    }

    public Build setCulprits(List<String> culprits) {
        this.culprits = culprits;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public Build setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getRepoName() {
        return repoName;
    }

    public Build setRepoName(String repoName) {
        this.repoName = repoName;
        return this;
    }

    public String getBranch() {
        return branch;
    }

    public Build setBranch(String branch) {
        this.branch = branch;
        return this;
    }

    public Boolean getLatest() {
        return latest;
    }

    public Build setLatest(Boolean latest) {
        this.latest = latest;
        return this;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Build setKeywords(List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    @Override
    public Object getId() {
        return this.id;
    }
}
