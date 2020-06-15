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

import static com.bbva.arq.devops.ae.mirrorgate.mapper.DashboardMapper.map;
import static com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus.ACTIVE;
import static com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus.DELETED;
import static com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus.TRANSIENT;

import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardConflictException;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardForbiddenException;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardNotFoundException;
import com.bbva.arq.devops.ae.mirrorgate.mapper.DashboardMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardServiceImpl.class);
    private static final Sort SORT_BY_LAST_MODIFICATION
        = Sort.by(Sort.Direction.DESC, "lastModification");

    private final DashboardRepository dashboardRepository;
    private final EventService eventService;

    @Autowired
    private DashboardServiceImpl(final DashboardRepository dashboardRepository, final EventService eventService) {
        this.dashboardRepository = dashboardRepository;
        this.eventService = eventService;
    }

    @Override
    public DashboardDTO getDashboard(final String name) {
        final Dashboard dashboard = getRepositoryDashboard(name);
        return map(dashboardRepository.save(dashboard.setLastTimeUsed(System.currentTimeMillis())));
    }

    private Dashboard getRepositoryDashboard(final String name) {
        final Dashboard dashboard = dashboardRepository.findFirstByName(name, SORT_BY_LAST_MODIFICATION);

        if (dashboard == null) {
            throw new DashboardNotFoundException("Dashboard not Found");
        }

        if (DELETED.equals(dashboard.getStatus())) {
            throw new DashboardNotFoundException("Dashboard was deleted");
        }
        return dashboard;
    }

    @Override
    public List<String> getApplicationsByDashboardName(final String name) {
        final DashboardDTO dashboard = this.getDashboard(name);
        return dashboard.getApplications();
    }

    @Override
    public List<DashboardDTO> getActiveAndTransientDashboards() {
        return dashboardRepository.getActiveAndTransientDashboards()
            .stream()
            .map(DashboardMapper::map)
            .collect(Collectors.toList());
    }

    @Override
    public List<DashboardDTO> getActiveDashboards() {
        return dashboardRepository.getActiveDashboards()
            .stream()
            .map(DashboardMapper::map)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteDashboard(final String name) {
        final Dashboard toDelete = this.getRepositoryDashboard(name);
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String authUser = "anonymous";

        if (auth != null) {
            authUser = (String) auth.getPrincipal();
            canEdit(authUser, toDelete);
        }

        toDelete.setStatus(DELETED);
        toDelete.setLastUserEdit(authUser);
        toDelete.setLastModification(System.currentTimeMillis());
        dashboardRepository.save(toDelete);
    }

    @Override
    public DashboardDTO newDashboard(final DashboardDTO dashboard) {

        if (dashboard.getName().isEmpty()) {
            throw new DashboardConflictException("Dashboard name must not be empty");
        }

        final Dashboard oldDashboard = dashboardRepository.findFirstByName(
            dashboard.getName(),
            SORT_BY_LAST_MODIFICATION
        );

        if (oldDashboard != null && oldDashboard.getStatus() != DELETED) {
            throw new DashboardConflictException("A Dashboard with name '" + dashboard.getName() + "' already exists");
        }

        if (dashboard.getStatus() != TRANSIENT) {
            dashboard.setStatus(ACTIVE);

            final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && null != auth.getPrincipal()) {
                dashboard.setAuthor(auth.getPrincipal().toString());
                dashboard.setLastUserEdit(auth.getPrincipal().toString());
                dashboard.setAdminUsers(Collections.singletonList(auth.getPrincipal().toString()));
            }
        }

        dashboard.setLastModification(System.currentTimeMillis());

        return map(dashboardRepository.save(map(dashboard)));
    }

    @Override
    public DashboardDTO newTransientDashboard(final DashboardDTO dashboard) {
        dashboard.setStatus(DashboardStatus.TRANSIENT);
        return newDashboard(dashboard);
    }

    @Override
    public DashboardDTO updateDashboard(final String dashboardName, final DashboardDTO updatedDashboard) {
        final Dashboard currentDashboard = this.getRepositoryDashboard(dashboardName);
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String authUser = "anonymous";

        if (auth != null) {
            authUser = (String) auth.getPrincipal();
            canEdit(authUser, currentDashboard);
        }

        if (updatedDashboard.getAdminUsers() == null) {
            updatedDashboard.setAdminUsers(Collections.singletonList(authUser));
        } else if (!updatedDashboard.getAdminUsers().contains(authUser)) {
            updatedDashboard.getAdminUsers().add(authUser);
        }

        final Dashboard toSave = mergeDashboard(currentDashboard, map(updatedDashboard), authUser);

        final Dashboard saved = dashboardRepository.save(toSave);

        eventService.saveEvent(saved, EventType.DETAIL);

        return map(saved);
    }

    @Override
    public void saveDashboardImage(final String dashboardName, final InputStream uploadFile) {
        final Dashboard currentDashboard = this.getRepositoryDashboard(dashboardName);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            final String authUser = (String) auth.getPrincipal();
            canEdit(authUser, currentDashboard);
        }
        dashboardRepository.saveFile(uploadFile, dashboardName);
    }

    @Override
    public InputStreamResource getDashboardImage(final String dashboardName) {
        //Used to ensure the dashboard is present and active
        this.getDashboard(dashboardName);
        return dashboardRepository.readFile(dashboardName);
    }

    @Override
    public List<Dashboard> getDashboardWithNames(final List<String> dashboardNames) {
        return dashboardRepository.findByNameIn(dashboardNames);
    }

    @Override
    public void createDashboardForBuildProject(final Build build) {

        final DashboardDTO newDashboard = new DashboardDTO();
        newDashboard.setCodeRepos(Collections.singletonList(build.getProjectName()));

        createTransientDashboard(newDashboard, build.getProjectName());
    }

    @Override
    public void createDashboardForJiraTeam(final String teamName) {

        final DashboardDTO newDashboard = new DashboardDTO();
        newDashboard.setBoards(Collections.singletonList(teamName));

        createTransientDashboard(newDashboard, teamName);
    }

    private void createTransientDashboard(final DashboardDTO newDashboard, final String identifier) {
        try {
            newDashboard.setName(identifier);
            newDashboard.setDisplayName(identifier);

            newTransientDashboard(newDashboard);
        } catch (DashboardConflictException e) {
            LOG.debug("Dashboard with name {} already exists", identifier);
        }
    }

    private Dashboard mergeDashboard(final Dashboard dashboard, final Dashboard request, final String principal) {

        request.setId(dashboard.getId());
        request.setLastUserEdit(principal);
        request.setLastModification(System.currentTimeMillis());

        if (request.getSlackToken() == null) {
            request.setSlackToken(dashboard.getSlackToken());
        }

        if (dashboard.getStatus() != null && dashboard.getStatus().equals(TRANSIENT)) {
            request.setStatus(ACTIVE);
        }

        return request;
    }

    private void canEdit(final String authUser, final Dashboard toEdit) {

        if (authUser == null) {
            throw new DashboardForbiddenException("Authenticated user not found");
        }

        if (toEdit.getAdminUsers() != null
            && toEdit.getAdminUsers().contains(authUser)) {
            return;
        }

        if (authUser.equals(toEdit.getAuthor())) {
            return;
        }

        if (toEdit.getAuthor() == null && (toEdit.getAdminUsers() == null
            || toEdit.getAdminUsers().isEmpty())) {
            return;
        }

        throw new DashboardForbiddenException("You do not have permissions to "
            + "perform this operation, please contact the Dashboard "
            + "administrator");
    }
}
