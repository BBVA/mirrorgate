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

import com.bbva.arq.devops.ae.mirrorgate.core.misc.MirrorGateException;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    @Override
    public Dashboard getDashboard(String name) {
        return dashboardRepository.findOneByName(name);
    }

    @Override
    public List<String> getReposByDashboardName(String name) {
        Dashboard dashboard = dashboardRepository.findOneByName(name);
        return dashboard == null ? null : dashboard.getCodeRepos();
    }

    @Override
    public List<String> getApplicationsByDashboardName(String name) {
        Dashboard dashboard = dashboardRepository.findOneByName(name);
        return dashboard == null ? null : dashboard.getApplications();
    }

    @Override
    public Iterable<Dashboard> getActiveDashboards() {
        return dashboardRepository.findByStatusNotOrStatusIsNull(DashboardStatus.DELETED);
    }

    @Override
    public Boolean deleteDashboard(String name) {
        Dashboard dashboard = dashboardRepository.findOneByName(name);
        if (dashboard != null) {
            dashboard.setStatus(DELETED);
            dashboard.setLastModification(System.currentTimeMillis());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && null != auth.getPrincipal()) {
                dashboard.setLastUserEdit(auth.getPrincipal().toString());
            }
            dashboardRepository.save(dashboard);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Dashboard newDashboard(Dashboard dashboard) throws MirrorGateException {
        Dashboard oldDashboard = dashboardRepository.findOneByName(dashboard.getName());
        if (oldDashboard != null) {
            if (oldDashboard.getStatus() != DELETED) {
                throw new MirrorGateException("Dashboard with name '" + dashboard.getName() + "' already exists");
            }
            dashboard.setId(oldDashboard.getId());
        }
        dashboard.setStatus(ACTIVE);
        dashboard.setLastModification(System.currentTimeMillis());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && null != auth.getPrincipal()) {
            dashboard.setLastUserEdit(auth.getPrincipal().toString());
        }
        return dashboardRepository.save(dashboard);
    }

    @Override
    public Dashboard updateDashboard(Dashboard dashboard) {
        Dashboard toUpdate = dashboardRepository.findOneByName(dashboard.getName());

        if(toUpdate == null) {
            return null;
        } else {
            dashboard.setId(toUpdate.getId());
            dashboard.setLastModification(System.currentTimeMillis());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && null != auth.getPrincipal()) {
                dashboard.setLastUserEdit(auth.getPrincipal().toString());
            }
            return dashboardRepository.save(dashboard);
        }
    }
}
