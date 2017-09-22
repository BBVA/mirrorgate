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

package com.bbva.arq.devops.ae.mirrorgate.core.dto;

import com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Filters;
import java.util.ArrayList;
import java.util.List;

/**
 * Dashboard Data Transfer Object
 */
public class DashboardDTO {

    private String name;
    private String displayName;
    private String logoUrl;
    private List<String> codeRepos = new ArrayList<>();
    private String sProductName; //Team product name
    private List<String> applications = new ArrayList<>();
    private List<String> adminUsers = new ArrayList<>();
    private List<String> boards;
    private Filters filters;
    private DashboardStatus status;
    private String lastUserEdit;
    private Long lastModification;
    private List<String> analyticViews = new ArrayList<>();

    private String skin = "classic";

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

    public void setLastModification(Long lastModification) {
        this.lastModification = lastModification;
    }

    public List<String> getAdminUsers() {
        return adminUsers;
    }

    public void setAdminUsers(List<String> adminUsers) {
        this.adminUsers = adminUsers;
    }

    public List<String> getAnalyticViews() {
        return analyticViews;
    }

    public void setAnalyticViews(List<String> analyticViews) {
        this.analyticViews = analyticViews;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }
}
