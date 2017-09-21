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
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;

/**
 * Created by alfonso on 28/05/17.
 */
public class DashboardMapper {

    private DashboardMapper(){}

    public static DashboardDTO map(Dashboard source) {
        return map(source, new DashboardDTO());
    }

    public static DashboardDTO map(Dashboard source, DashboardDTO target) {
        target.setAdminUsers(source.getAdminUsers());
        target.setApplications(source.getApplications());
        target.setBoards(source.getBoards());
        target.setCodeRepos(source.getCodeRepos());
        target.setDisplayName(source.getDisplayName());
        target.setFilters(source.getFilters());
        target.setLastModification(source.getLastModification());
        target.setLastUserEdit(source.getLastUserEdit());
        target.setLogoUrl(source.getLogoUrl());
        target.setName(source.getName());
        target.setsProductName(source.getsProductName());
        target.setStatus(source.getStatus());
        target.setAnalyticViews(source.getAnalyticViews());
        target.setSkin(source.getSkin());
        return target;
    }

}
