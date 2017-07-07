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
package com.bbva.arq.devops.ae.mirrorgate.service;

import static com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus.ACTIVE;
import static com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus.DELETED;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardConflictException;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardForbiddenException;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardNotFoundException;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    private static final Sort SORT_BY_LAST_MODIFICATION = new Sort(Sort.Direction.DESC, "lastModification");

    @Override
    public Dashboard getDashboard(String name) {
        Dashboard dashboard = dashboardRepository.findOneByName(name, SORT_BY_LAST_MODIFICATION);

        if (dashboard == null) {
            throw new DashboardNotFoundException("Dashboard not Found");
        }

        if (DELETED.equals(dashboard.getStatus())) {
            throw new DashboardNotFoundException("Dashboard was deleted");
        }

        return dashboard;
    }

    @Override
    public List<String> getReposByDashboardName(String name) {
        Dashboard dashboard = this.getDashboard(name);
        return dashboard.getCodeRepos();
    }

    @Override
    public List<String> getAdminUsersByDashboardName(String name) {
        Dashboard dashboard = this.getDashboard(name);
        return dashboard.getAdminUsers();
    }

    @Override
    public List<String> getApplicationsByDashboardName(String name) {
        Dashboard dashboard = this.getDashboard(name);
        return dashboard.getApplications();
    }

    @Override
    public List<DashboardDTO> getActiveDashboards() {
        return dashboardRepository.getActiveDashboards();
    }

    @Override
    public void deleteDashboard(String name) {
        Dashboard toDelete = this.getDashboard(name);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUser = (String)auth.getPrincipal();

        canEdit(authUser, toDelete);

        toDelete.setStatus(DELETED);
        toDelete.setLastUserEdit(auth.getPrincipal().toString());
        toDelete.setLastModification(System.currentTimeMillis());
        dashboardRepository.save(toDelete);
    }

    @Override
    public Dashboard newDashboard(Dashboard dashboard) {
        Dashboard oldDashboard = dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);
        if (oldDashboard != null && oldDashboard.getStatus() != DELETED) {
            throw new DashboardConflictException("A Dashboard with name '" + dashboard.getName() + "' already exists");
        }

        dashboard.setStatus(ACTIVE);
        dashboard.setLastModification(System.currentTimeMillis());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && null != auth.getPrincipal()) {
            dashboard.setAuthor(auth.getPrincipal().toString());
            dashboard.setLastUserEdit(auth.getPrincipal().toString());
        }
        dashboard.setLastModification(System.currentTimeMillis());

        return dashboardRepository.save(dashboard);
    }


    @Override
    public Dashboard updateDashboard(String name, Dashboard request) {
        Dashboard toUpdate = this.getDashboard(name);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authUser = (String)auth.getPrincipal();

        canEdit(authUser, toUpdate);

        Dashboard toSave = mergeDashboard(toUpdate, request, authUser);

        return dashboardRepository.save(toSave);
    }

    private Dashboard mergeDashboard(Dashboard dashboard, Dashboard request, String principal) {

        request.setId(dashboard.getId());
        request.setLastUserEdit(principal);
        request.setLastModification(System.currentTimeMillis());

        if(request.getSlackToken() == null) {
            request.setSlackToken(dashboard.getSlackToken());
        }

        return request;
    }

    private void canEdit(String authUser, Dashboard toEdit) {

        if (authUser == null) {
            throw new DashboardForbiddenException("Authenticated user not found");
        }

        if (toEdit.getAdminUsers().contains(authUser)) {
            return;
        }

        if (authUser.equals(toEdit.getAuthor())) {
            return;
        }

        if (toEdit.getAuthor() == null && toEdit.getAdminUsers().isEmpty()) {
            return;
        }

        throw new DashboardForbiddenException("You do not have permissions to perform this operation, please contact the Dashboard administrator");
    }
}
