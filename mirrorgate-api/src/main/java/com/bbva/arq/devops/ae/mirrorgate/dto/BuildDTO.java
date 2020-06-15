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

package com.bbva.arq.devops.ae.mirrorgate.dto;

import java.util.List;
import javax.validation.constraints.NotNull;

public class BuildDTO {

    @NotNull
    private String number;
    @NotNull
    private String buildUrl;
    @NotNull
    private String buildStatus;
    @NotNull
    private long startTime;

    private List<String> culprits;
    private long endTime;
    private Long timestamp;
    private long duration;

    private String projectName;
    private String repoName;
    private String branch;

    private List<String> keywords;

    public String getNumber() {
        return number;
    }

    public BuildDTO setNumber(final String number) {
        this.number = number;
        return this;
    }

    public String getBuildUrl() {
        return buildUrl;
    }

    public BuildDTO setBuildUrl(final String buildUrl) {
        this.buildUrl = buildUrl;
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    public BuildDTO setStartTime(final long startTime) {
        this.startTime = startTime;
        return this;
    }

    public Long getEndTime() {
        return endTime;
    }

    public BuildDTO setEndTime(final long endTime) {
        this.endTime = endTime;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public BuildDTO setDuration(final long duration) {
        this.duration = duration;
        return this;
    }

    public String getBuildStatus() {
        return buildStatus;
    }

    public BuildDTO setBuildStatus(final String buildStatus) {
        this.buildStatus = buildStatus;
        return this;
    }

    public List<String> getCulprits() {
        return culprits;
    }

    public BuildDTO setCulprits(final List<String> culprits) {
        this.culprits = culprits;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public BuildDTO setProjectName(final String projectName) {
        this.projectName = projectName;
        return this;
    }

    public String getRepoName() {
        return repoName;
    }

    public BuildDTO setRepoName(final String repoName) {
        this.repoName = repoName;
        return this;
    }

    public String getBranch() {
        return branch;
    }

    public BuildDTO setBranch(final String branch) {
        this.branch = branch;
        return this;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public BuildDTO setKeywords(final List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public BuildDTO setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}