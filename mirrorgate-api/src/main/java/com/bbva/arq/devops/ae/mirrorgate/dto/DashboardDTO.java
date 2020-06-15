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

import com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus;
import com.bbva.arq.devops.ae.mirrorgate.support.DashboardType;
import com.bbva.arq.devops.ae.mirrorgate.support.Filters;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Dashboard Data Transfer Object.
 */
public class DashboardDTO {

    private String name;
    private String displayName;
    private String logoUrl;
    private List<String> codeRepos;
    private String sProductName; //Team product name
    private List<String> applications;
    private List<String> adminUsers;
    private List<String> teamMembers;
    private List<String> boards;
    private Filters filters;
    private DashboardStatus status;
    private String lastUserEdit;
    private Long lastModification;
    private Long lastTimeUsed;
    private List<String> analyticViews;
    private List<String> operationViews;
    private Boolean infraCost;
    private String lastVersion;
    private Float responseTimeAlertingLevelWarning;
    private Float responseTimeAlertingLevelError;
    private Float errorsRateAlertingLevelWarning;
    private Float errorsRateAlertingLevelError;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String slackToken;
    private String slackTeam;
    private String slackChannel;

    private String programIncrement;

    private String skin = "classic";

    private String category;

    private String author;
    private String urlAlerts;
    private String urlAlertsAuthorization;
    private List<String> aggregatedDashboards;

    private DashboardType type = DashboardType.Detail;
    private List<List<String>> columns;

    private Integer marketsStatsDays = 7;

    private List<String> gitRepos;

    public String getName() {
        return name;
    }

    public DashboardDTO setName(final String name) {
        this.name = name;
        return this;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public DashboardDTO setLogoUrl(final String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public List<String> getCodeRepos() {
        return codeRepos;
    }

    public DashboardDTO setCodeRepos(final List<String> codeRepos) {
        this.codeRepos = codeRepos;
        return this;
    }

    public String getsProductName() {
        return sProductName;
    }

    public DashboardDTO setsProductName(final String sProductName) {
        this.sProductName = sProductName;
        return this;
    }

    public List<String> getApplications() {
        return applications;
    }

    public DashboardDTO setApplications(final List<String> applications) {
        this.applications = applications;
        return this;
    }

    public List<String> getBoards() {
        return boards;
    }

    public DashboardDTO setBoards(final List<String> boards) {
        this.boards = boards;
        return this;
    }

    public Filters getFilters() {
        return filters;
    }

    public DashboardDTO setFilters(final Filters filters) {
        this.filters = filters;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public DashboardDTO setDisplayName(final String displayName) {
        this.displayName = displayName;
        return this;
    }

    public DashboardStatus getStatus() {
        return status;
    }

    public DashboardDTO setStatus(final DashboardStatus status) {
        this.status = status;
        return this;
    }

    public String getLastUserEdit() {
        return lastUserEdit;
    }

    public DashboardDTO setLastUserEdit(final String lastUserEdit) {
        this.lastUserEdit = lastUserEdit;
        return this;
    }

    public Long getLastModification() {
        return lastModification;
    }

    public DashboardDTO setLastModification(final Long lastModification) {
        this.lastModification = lastModification;
        return this;
    }

    public Long getLastTimeUsed() {
        return lastTimeUsed;
    }

    public DashboardDTO setLastTimeUsed(final Long lastTimeUsed) {
        this.lastTimeUsed = lastTimeUsed;
        return this;
    }

    public List<String> getAdminUsers() {
        return adminUsers;
    }

    public DashboardDTO setAdminUsers(final List<String> adminUsers) {
        this.adminUsers = adminUsers;
        return this;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public DashboardDTO setTeamMembers(final List<String> teamMembers) {
        this.teamMembers = teamMembers;
        return this;
    }

    public List<String> getAnalyticViews() {
        return analyticViews;
    }

    public DashboardDTO setAnalyticViews(final List<String> analyticViews) {
        this.analyticViews = analyticViews;
        return this;
    }

    public List<String> getOperationViews() {
        return operationViews;
    }

    public DashboardDTO setOperationViews(final List<String> operationViews) {
        this.operationViews = operationViews;
        return this;
    }

    public Boolean getInfraCost() {
        return infraCost;
    }

    public DashboardDTO setInfraCost(final Boolean infraCost) {
        this.infraCost = infraCost;
        return this;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public DashboardDTO setLastVersion(final String lastVersion) {
        this.lastVersion = lastVersion;
        return this;
    }

    public Float getResponseTimeAlertingLevelWarning() {
        return responseTimeAlertingLevelWarning;
    }

    public DashboardDTO setResponseTimeAlertingLevelWarning(final Float responseTimeAlertingLevelWarning) {
        this.responseTimeAlertingLevelWarning = responseTimeAlertingLevelWarning;
        return this;
    }

    public Float getResponseTimeAlertingLevelError() {
        return responseTimeAlertingLevelError;
    }

    public DashboardDTO setResponseTimeAlertingLevelError(final Float responseTimeAlertingLevelError) {
        this.responseTimeAlertingLevelError = responseTimeAlertingLevelError;
        return this;
    }

    public Float getErrorsRateAlertingLevelWarning() {
        return errorsRateAlertingLevelWarning;
    }

    public DashboardDTO setErrorsRateAlertingLevelWarning(final Float errorsRateAlertingLevelWarning) {
        this.errorsRateAlertingLevelWarning = errorsRateAlertingLevelWarning;
        return this;
    }

    public Float getErrorsRateAlertingLevelError() {
        return errorsRateAlertingLevelError;
    }

    public DashboardDTO setErrorsRateAlertingLevelError(final Float errorsRateAlertingLevelError) {
        this.errorsRateAlertingLevelError = errorsRateAlertingLevelError;
        return this;
    }

    public String getSkin() {
        return skin;
    }

    public DashboardDTO setSkin(final String skin) {
        this.skin = skin;
        return this;
    }

    public String getSlackChannel() {
        return slackChannel;
    }

    public DashboardDTO setSlackChannel(final String slackChannel) {
        this.slackChannel = slackChannel;
        return this;
    }

    public String getSlackTeam() {
        return slackTeam;
    }

    public DashboardDTO setSlackTeam(final String slackTeam) {
        this.slackTeam = slackTeam;
        return this;
    }

    public String getSlackToken() {
        return slackToken;
    }

    public DashboardDTO setSlackToken(final String slackToken) {
        this.slackToken = slackToken;
        return this;
    }

    public String getProgramIncrement() {
        return programIncrement;
    }

    public DashboardDTO setProgramIncrement(final String programIncrement) {
        this.programIncrement = programIncrement;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public DashboardDTO setAuthor(final String author) {
        this.author = author;
        return this;
    }

    public String getUrlAlerts() {
        return urlAlerts;
    }

    public DashboardDTO setUrlAlerts(final String urlAlerts) {
        this.urlAlerts = urlAlerts;
        return this;
    }

    public DashboardDTO setUrlAlertsAuthorization(final String urlAlertsAuthorization) {
        this.urlAlertsAuthorization = urlAlertsAuthorization;
        return this;
    }

    public String getUrlAlertsAuthorization() {
        return urlAlertsAuthorization;
    }

    public DashboardDTO setAggregatedDashboards(final List<String> aggregatedDashboards) {
        this.aggregatedDashboards = aggregatedDashboards;
        return this;
    }

    public List<String> getAggregatedDashboards() {
        return aggregatedDashboards;
    }

    public DashboardType getType() {
        return type;
    }

    public DashboardDTO setType(final DashboardType type) {
        this.type = type;
        return this;
    }

    public List<List<String>> getColumns() {
        return columns;
    }

    public DashboardDTO setColumns(final List<List<String>> columns) {
        this.columns = columns;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public DashboardDTO setCategory(final String category) {
        this.category = category;
        return this;
    }

    public Integer getMarketsStatsDays() {
        return marketsStatsDays;
    }

    public DashboardDTO setMarketsStatsDays(final Integer marketsStatsDays) {
        this.marketsStatsDays = marketsStatsDays;
        return this;
    }

    public List<String> getGitRepos() {
        return gitRepos;
    }

    public DashboardDTO setGitRepos(final List<String> gitRepos) {
        this.gitRepos = gitRepos;
        return this;
    }
}
