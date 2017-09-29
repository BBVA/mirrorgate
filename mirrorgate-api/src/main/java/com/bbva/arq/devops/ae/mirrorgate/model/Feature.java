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

import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Team story model.
 *
 * Collectors:
 *   Jira
 */
@Document(collection = "feature")
public class Feature extends BaseModel{

    /* User story properties */
    private String sId;
    private String sNumber;
    private String sName;
    private String sTypeName;
    private String sStatus;
    private Double dEstimate;
    private String priority;

    /* Associated sprint properties */
    @Indexed
    private String sSprintID;
    private String sSprintName;

    @Indexed
    private String sSprintAssetState;
    private Date sprintBeginDate;
    private Date sprintEndDate;

    /* Associated project properties */
    private String sProjectId;

    /* Associated PI properties */
    private List<String> sPiNames;

    @Indexed
    private String sParentKey;
    private Long lParentId;

    /* Associated collector ID */
    private String collectorId;

    @Indexed
    private String sProjectName;

    @Indexed
    private List<String> keywords;

    public String getsId() {
        return sId;
    }
    public Feature setsId(String sId) {
        this.sId = sId;
        return this;
    }
    public String getsNumber() {
        return sNumber;
    }
    public Feature setsNumber(String sNumber) {
        this.sNumber = sNumber;
        return this;
    }
    public String getsName() {
        return sName;
    }
    public Feature setsName(String sName) {
        this.sName = sName;
        return this;
    }
    public String getsTypeName() {
        return sTypeName;
    }
    public Feature setsTypeName(String sTypeName) {
        this.sTypeName = sTypeName;
        return this;
    }
    public String getsStatus() {
        return sStatus;
    }
    public Feature setsStatus(String sStatus) {
        this.sStatus = sStatus;
        return this;
    }
    public String getsSprintID() {
        return sSprintID;
    }
    public Feature setsSprintID(String sSprintID) {
        this.sSprintID = sSprintID;
        return this;
    }
    public String getsSprintName() {
        return sSprintName;
    }
    public Feature setsSprintName(String sSprintName) {
        this.sSprintName = sSprintName;
        return this;
    }
    public String getsSprintAssetState() {
        return sSprintAssetState;
    }
    public Feature setsSprintAssetState(String sSprintAssetState) {
        this.sSprintAssetState = sSprintAssetState;
        return this;
    }
    public Date getSprintBeginDate() {
        return sprintBeginDate;
    }
    public Feature setSprintBeginDate(Date sprintBeginDate) {
        this.sprintBeginDate = sprintBeginDate;
        return this;
    }
    public Date getSprintEndDate() {
        return sprintEndDate;
    }
    public Feature setSprintEndDate(Date sprintEndDate) {
        this.sprintEndDate = sprintEndDate;
        return this;
    }
    public String getsProjectId() {
        return sProjectId;
    }
    public Feature setsProjectId(String sProjectId) {
        this.sProjectId = sProjectId;
        return this;
    }
    public String getsProjectName() {
        return sProjectName;
    }
    public Feature setsProjectName(String sProjectName) {
        this.sProjectName = sProjectName;
        return this;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Feature setKeywords(List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    public Double getdEstimate() {
        return dEstimate;
    }

    public Feature setdEstimate(Double dEstimate) {
        this.dEstimate = dEstimate;
        return this;
    }

    public String getPriority() {
        return priority;
    }

    public Feature setPriority(String priority) {
        this.priority = priority;
        return this;
    }

    public String getsParentKey() {
        return sParentKey;
    }

    public Feature setsParentKey(String sParentKey) {
        this.sParentKey = sParentKey;
        return this;
    }

    public Long getlParentId() {
        return lParentId;
    }

    public Feature setlParentId(Long lParentId) {
        this.lParentId = lParentId;
        return this;
    }

    public List<String> getsPiNames() {
        return sPiNames;
    }

    public Feature setsPiNames(List<String> sPiNames) {
        this.sPiNames = sPiNames;
        return this;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public Feature setCollectorId(String collectorId) {
        this.collectorId = collectorId;
        return this;
    }

}
