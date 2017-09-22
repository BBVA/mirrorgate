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
import static com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus.TRANSIENT;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.ImageStream;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardConflictException;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardForbiddenException;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardNotFoundException;
import com.bbva.arq.devops.ae.mirrorgate.mapper.DashboardMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private static final Sort SORT_BY_LAST_MODIFICATION = new Sort(Sort.Direction.DESC, "lastModification");

    private DashboardRepository dashboardRepository;


    @Autowired
    public DashboardServiceImpl(DashboardRepository dashboardRepository){

        this.dashboardRepository = dashboardRepository;
    }

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
        return dashboardRepository.getActiveDashboards().stream().map(DashboardMapper::map).collect(Collectors.toList());
    }

    @Override
    public void deleteDashboard(String name) {
        Dashboard toDelete = this.getDashboard(name);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String authUser = "anonymous";

        if(auth != null) {
            authUser = (String) auth.getPrincipal();
            canEdit(authUser, toDelete);
        }

        toDelete.setStatus(DELETED);
        toDelete.setLastUserEdit(authUser);
        toDelete.setLastModification(System.currentTimeMillis());
        dashboardRepository.save(toDelete);
    }

    @Override
    public Dashboard newDashboard(Dashboard dashboard) {
        Dashboard oldDashboard = dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);

        if (oldDashboard != null && oldDashboard.getStatus() != DELETED) {
            throw new DashboardConflictException("A Dashboard with name '" + dashboard.getName() + "' already exists");
        }

        if(dashboard.getStatus() != TRANSIENT) {
            dashboard.setStatus(ACTIVE);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && null != auth.getPrincipal()) {
                dashboard.setAuthor(auth.getPrincipal().toString());
                dashboard.setLastUserEdit(auth.getPrincipal().toString());
            }
        }

        dashboard.setLastModification(System.currentTimeMillis());

        return dashboardRepository.save(dashboard);
    }


    @Override
    public Dashboard updateDashboard(String dashboardName, Dashboard updatedDashboard) {
        Dashboard currentDashboard = this.getDashboard(dashboardName);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String authUser = "anonymous";

        if(auth != null) {
            authUser = (String) auth.getPrincipal();
            canEdit(authUser, currentDashboard);
        }

        if(null != updatedDashboard.getAdminUsers() && !updatedDashboard.getAdminUsers().contains(authUser))
            updatedDashboard.getAdminUsers().add(authUser);

        Dashboard toSave = mergeDashboard(currentDashboard, updatedDashboard, authUser);

        return dashboardRepository.save(toSave);
    }

    @Override
    public void saveDashboardImage(String dashboardName, InputStream uploadfile) {
        Dashboard currentDashboard = this.getDashboard(dashboardName);

        if(currentDashboard != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null) {
                String authUser = (String) auth.getPrincipal();
                canEdit(authUser, currentDashboard);
            }
        }
        dashboardRepository.saveFile(uploadfile, dashboardName);
    }

    @Override
    public ImageStream getDashboardImage(String dashboardName) {
        //Used to ensure the dashboard is present and active
        this.getDashboard(dashboardName);
        return dashboardRepository.readFile(dashboardName);
    }

    private Dashboard mergeDashboard(Dashboard dashboard, Dashboard request, String principal) {

        request.setId(dashboard.getId());
        request.setLastUserEdit(principal);
        request.setLastModification(System.currentTimeMillis());

        if(request.getSlackToken() == null) {
            request.setSlackToken(dashboard.getSlackToken());
        }

        if(dashboard.getStatus() !=null && dashboard.getStatus().equals(TRANSIENT)){
            request.setStatus(ACTIVE);
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
