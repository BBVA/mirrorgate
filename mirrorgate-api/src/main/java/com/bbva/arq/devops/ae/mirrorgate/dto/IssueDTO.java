/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.dto;

import com.bbva.arq.devops.ae.mirrorgate.support.IssuePriority;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class IssueDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @NotNull
    private IssueStatus status;

    private IssuePriority priority;
    private Double estimate = 0.0;
    private String jiraKey;
    private List<String> parentKey;
    private List<String> parentId;
    private List<String> piNames;

    private SprintDTO sprint;
    private ProjectDTO project;

    private String url;

    @NotNull
    private Date updatedDate;

    private List<String> keywords = null;

    private String collectorId;

    private String teamName;

    public Long getId() {
        return id;
    }

    public IssueDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public IssueDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public IssueDTO setType(String type) {
        this.type = type;
        return this;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public IssueDTO setStatus(IssueStatus status) {
        this.status = status;
        return this;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public IssueDTO setPriority(IssuePriority priority) {
        this.priority = priority;
        return this;
    }

    public String getJiraKey() {
        return jiraKey;
    }

    public IssueDTO setJiraKey(String jiraKey) {
        this.jiraKey = jiraKey;
        return this;
    }

    public Double getEstimate() {
        return estimate;
    }

    public IssueDTO setEstimate(Double estimate) {
        this.estimate = estimate;
        return this;
    }

    public List<String> getParentKey() {
        return parentKey;
    }

    public IssueDTO setParentKey(List<String> parentKey) {
        this.parentKey = parentKey;
        return this;
    }

    public List<String> getParentId() {
        return parentId;
    }

    public IssueDTO setParentId(List<String> parentId) {
        this.parentId = parentId;
        return this;
    }

    public List<String> getPiNames() {
        return piNames;
    }

    public IssueDTO setPiNames(List<String> piNames) {
        this.piNames = piNames;
        return this;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public IssueDTO setKeywords(List<String> keyword) {
        this.keywords = keyword;
        return this;
    }

    public SprintDTO getSprint() {
        return sprint;
    }

    public IssueDTO setSprint(SprintDTO sprint) {
        this.sprint = sprint;
        return this;
    }

    @Override
    public String toString() {
        return name + " - " + getEstimate();
    }

    public ProjectDTO getProject() {
        return project;
    }

    public IssueDTO setProject(ProjectDTO project) {
        this.project = project;
        return this;
    }

    public Date getUpdatedDate() {
        return new Date(updatedDate.getTime());
    }

    public IssueDTO setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate == null ? null : new Date(updatedDate.getTime());
        return this;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public IssueDTO setCollectorId(String collectorId) {
        this.collectorId = collectorId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public IssueDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTeamName() {
        return teamName;
    }

    public IssueDTO setTeamName(String teamName) {
        this.teamName = teamName;
        return this;
    }
}
