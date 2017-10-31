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

package com.bbva.arq.devops.ae.mirrorgate.mapper;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardType;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;

public class DashboardMapper {

    private DashboardMapper(){}

    public static DashboardDTO map(Dashboard source) {
        return map(source, new DashboardDTO());
    }

    public static Dashboard map(DashboardDTO source) {
        return map(source, new Dashboard());
    }

    public static DashboardDTO map(Dashboard source, DashboardDTO target) {
        return target.setAdminUsers(source.getAdminUsers())
                .setApplications(source.getApplications())
                .setType(source.getType() == null ? null : DashboardType.valueOf(source.getType()))
                .setName(source.getName())
                .setBoards(source.getBoards())
                .setCodeRepos(source.getCodeRepos())
                .setDisplayName(source.getDisplayName())
                .setFilters(source.getFilters())
                .setLastModification(source.getLastModification())
                .setLastUserEdit(source.getLastUserEdit())
                .setTeamMembers(source.getTeamMembers())
                .setLogoUrl(source.getLogoUrl())
                .setName(source.getName())
                .setsProductName(source.getsProductName())
                .setStatus(source.getStatus())
                .setAnalyticViews(source.getAnalyticViews())
                .setLastVersion(source.getLastVersion())
                .setResponseTimeAlertingLevelWarning(source.getResponseTimeAlertingLevelWarning())
                .setResponseTimeAlertingLevelError(source.getResponseTimeAlertingLevelError())
                .setErrorsRateAlertingLevelWarning(source.getErrorsRateAlertingLevelWarning())
                .setErrorsRateAlertingLevelError(source.getErrorsRateAlertingLevelError())
                .setAuthor(source.getAuthor())
                .setSlackToken(source.getSlackToken())
                .setSlackTeam(source.getSlackTeam())
                .setSlackChannel(source.getSlackChannel())
                .setProgramIncrement(source.getProgramIncrement())
                .setUrlAlerts(source.getUrlAlerts())
                .setUrlAlertsAuthorization(source.getUrlAlertsAuthorization())
                .setAggregatedDashboards(source.getAggregatedDashboards())
                .setSkin(source.getSkin())
                .setCategory(source.getCategory());
    }

    public static Dashboard map(DashboardDTO source, Dashboard target) {
        return target.setAdminUsers(source.getAdminUsers())
                .setApplications(source.getApplications())
                .setName(source.getName())
                .setType(source.getType() == null ? null : source.getType().name())
                .setBoards(source.getBoards())
                .setCodeRepos(source.getCodeRepos())
                .setDisplayName(source.getDisplayName())
                .setFilters(source.getFilters())
                .setLastModification(source.getLastModification())
                .setLastUserEdit(source.getLastUserEdit())
                .setTeamMembers(source.getTeamMembers())
                .setLogoUrl(source.getLogoUrl())
                .setName(source.getName())
                .setsProductName(source.getsProductName())
                .setStatus(source.getStatus())
                .setAnalyticViews(source.getAnalyticViews())
                .setLastVersion(source.getLastVersion())
                .setResponseTimeAlertingLevelWarning(source.getResponseTimeAlertingLevelWarning())
                .setResponseTimeAlertingLevelError(source.getResponseTimeAlertingLevelError())
                .setErrorsRateAlertingLevelWarning(source.getErrorsRateAlertingLevelWarning())
                .setErrorsRateAlertingLevelError(source.getErrorsRateAlertingLevelError())
                .setSlackToken(source.getSlackToken())
                .setSlackTeam(source.getSlackTeam())
                .setSlackChannel(source.getSlackChannel())
                .setProgramIncrement(source.getProgramIncrement())
                .setAuthor(source.getAuthor())
                .setUrlAlerts(source.getUrlAlerts())
                .setUrlAlertsAuthorization(source.getUrlAlertsAuthorization())
                .setAggregatedDashboards(source.getAggregatedDashboards())
                .setSkin(source.getSkin())
                .setCategory(source.getCategory());
    }

}
