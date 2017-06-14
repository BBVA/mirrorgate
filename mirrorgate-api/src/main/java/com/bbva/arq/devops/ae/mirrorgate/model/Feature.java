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

import java.util.List;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

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
    private String sParentKey;
    private Long lParentId;

    @Indexed
    private String sProjectName;

    @Indexed
    private List<String> keywords;

    public String getsId() {
        return sId;
    }
    public void setsId(String sId) {
        this.sId = sId;
    }
    public String getsNumber() {
        return sNumber;
    }
    public void setsNumber(String sNumber) {
        this.sNumber = sNumber;
    }
    public String getsName() {
        return sName;
    }
    public void setsName(String sName) {
        this.sName = sName;
    }
    public String getsTypeName() {
        return sTypeName;
    }
    public void setsTypeName(String sTypeName) {
        this.sTypeName = sTypeName;
    }
    public String getsStatus() {
        return sStatus;
    }
    public void setsStatus(String sStatus) {
        this.sStatus = sStatus;
    }
    public String getsSprintID() {
        return sSprintID;
    }
    public void setsSprintID(String sSprintID) {
        this.sSprintID = sSprintID;
    }
    public String getsSprintName() {
        return sSprintName;
    }
    public void setsSprintName(String sSprintName) {
        this.sSprintName = sSprintName;
    }
    public String getsSprintAssetState() {
        return sSprintAssetState;
    }
    public void setsSprintAssetState(String sSprintAssetState) {
        this.sSprintAssetState = sSprintAssetState;
    }
    public Date getSprintBeginDate() {
        return sprintBeginDate;
    }
    public void setSprintBeginDate(Date sprintBeginDate) {
        this.sprintBeginDate = sprintBeginDate;
    }
    public Date getSprintEndDate() {
        return sprintEndDate;
    }
    public void setSprintEndDate(Date sprintEndDate) {
        this.sprintEndDate = sprintEndDate;
    }
    public String getsProjectId() {
        return sProjectId;
    }
    public void setsProjectId(String sProjectId) {
        this.sProjectId = sProjectId;
    }
    public String getsProjectName() {
        return sProjectName;
    }
    public void setsProjectName(String sProjectName) {
        this.sProjectName = sProjectName;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public Double getdEstimate() {
        return dEstimate;
    }

    public void setdEstimate(Double dEstimate) {
        this.dEstimate = dEstimate;
    }

    public String getsParentKey() {
        return sParentKey;
    }

    public void setsParentKey(String sParentKey) {
        this.sParentKey = sParentKey;
    }

    public Long getlParentId() {
        return lParentId;
    }

    public void setlParentId(Long lParentId) {
        this.lParentId = lParentId;
    }

    public List<String> getsPiNames() {
        return sPiNames;
    }

    public void setsPiNames(List<String> sPiNames) {
        this.sPiNames = sPiNames;
    }
}
