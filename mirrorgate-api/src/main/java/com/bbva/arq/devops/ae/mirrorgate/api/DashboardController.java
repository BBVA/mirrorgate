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
package com.bbva.arq.devops.ae.mirrorgate.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import java.util.List;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dashboards controller.
 */
@RestController
public class DashboardController {

    private static final Logger LOG = Logger.getLogger(DashboardController.class.getName());

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @RequestMapping(
            value = "/dashboards/{name}/details",
            method = GET,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getDashboard(@PathVariable("name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getDashboard(name));
    }

    @RequestMapping(
            value = "/dashboards/{name}",
            method = DELETE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteDashboard(@PathVariable("name") String name) {
        dashboardService.deleteDashboard(name);
        return ResponseEntity.status(HttpStatus.OK).body("Dashboard was delted successfully");
    }

    @RequestMapping(value = "/dashboards", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<DashboardDTO> getActiveDashboards() {
        return dashboardService.getActiveDashboards();
    }

    @RequestMapping(value = "/dashboards", method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> newDashboard(@Valid @RequestBody Dashboard request) {
        return ResponseEntity.ok(dashboardService.newDashboard(request));
    }

    @RequestMapping(value = "/dashboards/{name}", method = PUT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDashboard(
            @PathVariable("name") String name,
            @Valid @RequestBody Dashboard request) {

        Dashboard dashboard = dashboardService.getDashboard(name);

        dashboard.setApplications(request.getApplications());
        dashboard.setBoards(request.getBoards());
        dashboard.setCodeRepos(request.getCodeRepos());
        dashboard.setDisplayName(request.getDisplayName());
        dashboard.setFilters(request.getFilters());
        dashboard.setLogoUrl(request.getLogoUrl());
        dashboard.setSlackTeam(request.getSlackTeam());
        dashboard.setSlackChannel(request.getSlackChannel());
        dashboard.setProgramIncrement(request.getProgramIncrement());
        if(request.getSlackToken() != null) {
            dashboard.setSlackToken(request.getSlackToken());
        }
        dashboard.setsProductName(request.getsProductName());
        dashboard.setUrlAlerts(request.getUrlAlerts());
        dashboard = dashboardService.updateDashboard(dashboard);

        return ResponseEntity.ok(dashboard);
    }
}
