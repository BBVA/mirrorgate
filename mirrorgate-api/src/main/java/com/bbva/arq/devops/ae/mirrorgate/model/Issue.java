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

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


/**
 * Team story model.
 * <p>
 * Collectors:
 * Jira
 */
@Document(collection = "issue")
@CompoundIndexes({
    @CompoundIndex(name = "stats", def = "{ 'keywords': 1, 'type': 1, 'sprintAssetState': 1, 'status': 1 }"),
    @CompoundIndex(name = "programIncrement", def = "{ 'type': 1, 'piNames': 1 }"),
    @CompoundIndex(name = "boards", def = "{ 'projectName' : 1, 'keywords' : 1, 'sprintAssetState': 1 }"),
    @CompoundIndex(name = "idAndCollector", def = " { 'issueId': 1, 'collectorId': 1} "),
    @CompoundIndex(name = "epics", def = " {'number': 1, 'type': 1} "),
    @CompoundIndex(name = "sprint", def = " { 'sprintId': 1, 'collectorId': 1} "),
    @CompoundIndex(name = "sprintStatus", def = " { 'sprintAssetState': 1, 'collectorId': 1} ")
})
public class Issue implements BaseModel {

    @Id
    private String id;

    private String issueId;
    private List<String> keywords;
    private String number;
    private String name;
    private String type;
    private String status;
    private Double estimation;
    private String priority;
    private String url;
    private Long timestamp;
    @Indexed
    private List<String> parentsKeys;
    private List<Long> parentsIds;

    /* Associated sprint properties */
    private String sprintId;
    private String sprintName;
    private String sprintAssetState;
    private Date sprintBeginDate;
    private Date sprintEndDate;

    /* Associated project properties */
    private String projectId;
    private String projectName;

    /* Associated PI properties */
    private List<String> piNames;

    /* Associated team properties */
    private String teamName;

    /* Associated collector ID */
    private String collectorId;

    public String getIssueId() {
        return issueId;
    }

    public Issue setIssueId(final String issueId) {
        this.issueId = issueId;
        this.id = collectorId == null ? issueId : issueId + "-" + collectorId;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public Issue setNumber(final String number) {
        this.number = number;
        return this;
    }

    public String getName() {
        return name;
    }

    public Issue setName(final String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Issue setType(final String type) {
        this.type = type;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Issue setStatus(final String status) {
        this.status = status;
        return this;
    }

    public String getSprintId() {
        return sprintId;
    }

    public Issue setSprintId(final String sprintId) {
        this.sprintId = sprintId;
        return this;
    }

    public String getSprintName() {
        return sprintName;
    }

    public Issue setSprintName(final String sprintName) {
        this.sprintName = sprintName;
        return this;
    }

    public String getSprintAssetState() {
        return sprintAssetState;
    }

    public Issue setSprintAssetState(final String sprintAssetState) {
        this.sprintAssetState = sprintAssetState;
        return this;
    }

    public Date getSprintBeginDate() {
        return sprintBeginDate == null ? null : new Date(sprintBeginDate.getTime());
    }

    public Issue setSprintBeginDate(final Date sprintBeginDate) {
        this.sprintBeginDate = sprintBeginDate == null ? null : new Date(sprintBeginDate.getTime());
        return this;
    }

    public Date getSprintEndDate() {
        return sprintEndDate == null ? null : new Date(sprintEndDate.getTime());
    }

    public Issue setSprintEndDate(final Date sprintEndDate) {
        this.sprintEndDate = sprintEndDate == null ? null : new Date(sprintEndDate.getTime());
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public Issue setProjectId(final String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public Issue setProjectName(final String projectName) {
        this.projectName = projectName;
        return this;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Issue setKeywords(final List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    public Double getEstimation() {
        return estimation;
    }

    public Issue setEstimation(final Double estimation) {
        this.estimation = estimation;
        return this;
    }

    public String getPriority() {
        return priority;
    }

    public Issue setPriority(final String priority) {
        this.priority = priority;
        return this;
    }

    public List<String> getParentsKeys() {
        return parentsKeys;
    }

    public Issue setParentsKeys(final List<String> parentsKeys) {
        this.parentsKeys = parentsKeys;
        return this;
    }

    public List<Long> getParentsIds() {
        return parentsIds;
    }

    public Issue setParentsIds(final List<Long> parentsIds) {
        this.parentsIds = parentsIds;
        return this;
    }

    public List<String> getPiNames() {
        return piNames;
    }

    public Issue setPiNames(final List<String> piNames) {
        this.piNames = piNames;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Issue setUrl(final String url) {
        this.url = url;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Issue setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getTeamName() {
        return teamName;
    }

    public Issue setTeamName(final String teamName) {
        this.teamName = teamName;
        return this;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public Issue setCollectorId(final String collectorId) {
        this.collectorId = collectorId;
        this.id = this.issueId == null ? null : this.issueId + (collectorId == null ? "" : "-" + collectorId);
        return this;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
