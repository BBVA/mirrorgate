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
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Indexed
    private List<String> codeRepos;
    private List<String> adminUsers;
    private List<String> teamMembers;
    private String sProductName; //Team product name
    private List<String> applications;
    private List<String> analyticViews;
    private List<String> boards;
    private Filters filters;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<String> getCodeRepos() {
        return codeRepos;
    }

    public void setCodeRepos(List<String> codeRepos) {
        this.codeRepos = codeRepos;
    }

    public String getsProductName() {
        return sProductName;
    }

    public void setsProductName(String sProductName) {
        this.sProductName = sProductName;
    }

    public List<String> getApplications() {
        return applications;
    }

    public void setApplications(List<String> applications) {
        this.applications = applications;
    }

    public List<String> getBoards() {
        return boards;
    }

    public void setBoards(List<String> boards) {
        this.boards = boards;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public DashboardStatus getStatus() {
        return status;
    }

    public void setStatus(DashboardStatus status) {
        this.status = status;
    }

    public String getLastUserEdit() {
        return lastUserEdit;
    }

    public void setLastUserEdit(String lastUserEdit) {
        this.lastUserEdit = lastUserEdit;
    }

    public Long getLastModification() {
        return lastModification;
    }

    public void setLastModification(long lastModification) {
        this.lastModification = lastModification;
    }

    public String getSlackToken() {
        return slackToken;
    }

    public void setSlackToken(String slackToken) {
        this.slackToken = slackToken;
    }

    public String getSlackTeam() {
        return slackTeam;
    }

    public void setSlackTeam(String slackTeam) {
        this.slackTeam = slackTeam;
    }

    public String getProgramIncrement() {
        return programIncrement;
    }

    public void setProgramIncrement(String programIncrement) {
        this.programIncrement = programIncrement;
    }

    public String getUrlAlerts() {
        return urlAlerts;
    }

    public void setUrlAlerts(String urlAlerts) {
        this.urlAlerts = urlAlerts;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSlackChannel() {
        return slackChannel;
    }

    public void setSlackChannel(String slackChannel) {
        this.slackChannel = slackChannel;
    }

    public List<String> getAdminUsers() {
        return adminUsers;
    }

    public void setAdminUsers(List<String> adminUsers) {
        this.adminUsers = adminUsers;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<String> getAnalyticViews() {
        return analyticViews;
    }

    public void setAnalyticViews(List<String> analyticViews) {
        this.analyticViews = analyticViews;
    }

    public String getUrlAlertsAuthorization() {
        return urlAlertsAuthorization;
    }

    public void setUrlAlertsAuthorization(String urlAlertsAuthorization) {
        this.urlAlertsAuthorization = urlAlertsAuthorization;
    }
}
