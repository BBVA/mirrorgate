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

import com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Filters;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Dashboard info model
 */
@Document(collection = "dashboards")
public class Dashboard extends BaseModel {

    @Indexed
    @NotNull(message = "Dashboard name must not be null")
    private String name;
    private String displayName;
    private String logoUrl;
    private String type;

    @Indexed
    private List<String> codeRepos;
    private List<String> adminUsers;
    private List<String> teamMembers;
    private String sProductName; //Team product name
    private List<String> applications;
    private List<String> analyticViews;
    private String lastVersion;
    private Float responseTimeAlertingLevelWarning;
    private Float responseTimeAlertingLevelError;
    private Float errorsRateAlertingLevelWarning;
    private Float errorsRateAlertingLevelError;
    private List<String> boards;
    private Filters filters;

    private String slackToken;
    private String slackTeam;
    private String slackChannel;
    private DashboardStatus status;
    private String lastUserEdit;
    @Indexed
    private Long lastModification;
    private String programIncrement;
    private String urlAlerts;
    private String urlAlertsAuthorization;
    private String author;
    private String skin = "classic";
    private String category;

    private List<String> aggregatedDashboards;

    private Integer marketsStatsDays = 7;


    public String getName() {
        return name;
    }
    public Dashboard setName(String name) {
        this.name = name;
        return this;
    }

    public String getLogoUrl() {
        return logoUrl;
    }
    public Dashboard setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public List<String> getCodeRepos() {
        return codeRepos;
    }
    public Dashboard setCodeRepos(List<String> codeRepos) {
        this.codeRepos = codeRepos;
        return this;
    }

    public String getsProductName() {
        return sProductName;
    }
    public Dashboard setsProductName(String sProductName) {
        this.sProductName = sProductName;
        return this;
    }

    public List<String> getApplications() {
        return applications;
    }

    public Dashboard setApplications(List<String> applications) {
        this.applications = applications;
        return this;
    }

    public List<String> getBoards() {
        return boards;
    }

    public Dashboard setBoards(List<String> boards) {
        this.boards = boards;
        return this;
    }

    public Filters getFilters() {
        return filters;
    }

    public Dashboard setFilters(Filters filters) {
        this.filters = filters;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Dashboard setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public DashboardStatus getStatus() {
        return status;
    }

    public Dashboard setStatus(DashboardStatus status) {
        this.status = status;
        return this;
    }

    public String getLastUserEdit() {
        return lastUserEdit;
    }

    public Dashboard setLastUserEdit(String lastUserEdit) {
        this.lastUserEdit = lastUserEdit;
        return this;
    }

    public Long getLastModification() {
        return lastModification;
    }

    public Dashboard setLastModification(Long lastModification) {
        this.lastModification = lastModification;
        return this;
    }

    public String getSlackToken() {
        return slackToken;
    }

    public Dashboard setSlackToken(String slackToken) {
        this.slackToken = slackToken;
        return this;
    }

    public String getSlackTeam() {
        return slackTeam;
    }

    public Dashboard setSlackTeam(String slackTeam) {
        this.slackTeam = slackTeam;
        return this;
    }

    public String getProgramIncrement() {
        return programIncrement;
    }

    public Dashboard setProgramIncrement(String programIncrement) {
        this.programIncrement = programIncrement;
        return this;
    }

    public String getUrlAlerts() {
        return urlAlerts;
    }

    public Dashboard setUrlAlerts(String urlAlerts) {
        this.urlAlerts = urlAlerts;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Dashboard setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getSlackChannel() {
        return slackChannel;
    }

    public Dashboard setSlackChannel(String slackChannel) {
        this.slackChannel = slackChannel;
        return this;
    }

    public List<String> getAdminUsers() {
        return adminUsers;
    }

    public Dashboard setAdminUsers(List<String> adminUsers) {
        this.adminUsers = adminUsers;
        return this;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public Dashboard setTeamMembers(List<String> teamMembers) {
        this.teamMembers = teamMembers;
        return this;
    }

    public List<String> getAnalyticViews() {
        return analyticViews;
    }

    public Dashboard setAnalyticViews(List<String> analyticViews) {
        this.analyticViews = analyticViews;
        return this;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public Dashboard setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
        return this;
    }

    public Float getResponseTimeAlertingLevelWarning() {
        if(responseTimeAlertingLevelWarning == null){
            return .3f;
        }
        return responseTimeAlertingLevelWarning;
    }

    public Dashboard setResponseTimeAlertingLevelWarning(Float responseTimeAlertingLevelWarning) {
        this.responseTimeAlertingLevelWarning = responseTimeAlertingLevelWarning;
        return this;
    }

    public Float getResponseTimeAlertingLevelError() {
        if(responseTimeAlertingLevelError == null){
            return .5f;
        }
        return responseTimeAlertingLevelError;
    }

    public Dashboard setResponseTimeAlertingLevelError(Float responseTimeAlertingLevelError) {
        this.responseTimeAlertingLevelError = responseTimeAlertingLevelError;
        return this;
    }

    public Float getErrorsRateAlertingLevelWarning() {
        if(errorsRateAlertingLevelWarning == null){
            return .3f;
        }
        return errorsRateAlertingLevelWarning;
    }

    public Dashboard setErrorsRateAlertingLevelWarning(Float errorsRateAlertingLevelWarning) {
        this.errorsRateAlertingLevelWarning = errorsRateAlertingLevelWarning;
        return this;
    }

    public Float getErrorsRateAlertingLevelError() {
        if(errorsRateAlertingLevelError == null){
            return .5f;
        }
        return errorsRateAlertingLevelError;
    }

    public Dashboard setErrorsRateAlertingLevelError(Float errorsRateAlertingLevelError) {
        this.errorsRateAlertingLevelError = errorsRateAlertingLevelError;
        return this;
    }

    public String getUrlAlertsAuthorization() {
        return urlAlertsAuthorization;
    }

    public Dashboard setUrlAlertsAuthorization(String urlAlertsAuthorization) {
        this.urlAlertsAuthorization = urlAlertsAuthorization;
        return this;
    }

    public String getSkin() {
        return skin;
    }

    public Dashboard setSkin(String skin) {
        this.skin = skin;
        return this;
    }

    public List<String> getAggregatedDashboards() {
        return aggregatedDashboards;
    }

    public Dashboard setAggregatedDashboards(List<String> aggregatedDashboards) {
        this.aggregatedDashboards = aggregatedDashboards;
        return this;
    }

    public String getType() {
        return type;
    }

    public Dashboard setType(String type) {
        this.type = type;
        return this;
    }

    public String getCategory() {
        return category;
    }
    public Dashboard setCategory(String category) {
        this.category = category;
        return this;
    }

    public Integer getMarketsStatsDays() {
        return marketsStatsDays;
    }
    public Dashboard setMarketsStatsDays(Integer marketsStatsDays) {
        this.marketsStatsDays = marketsStatsDays;
        return this;
    }
}
