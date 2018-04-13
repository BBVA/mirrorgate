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
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.bbva.arq.devops.ae.mirrorgate.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Build controller.
 */
@RestController
public class BuildController {

    private final BuildService buildService;
    private final DashboardService dashboardService;

    @Autowired
    public BuildController(BuildService buildService, DashboardService dashboardService) {
        this.buildService = buildService;
        this.dashboardService = dashboardService;
    }

    @RequestMapping(value = "/dashboards/{name}/builds", method = GET,
            produces = APPLICATION_JSON_VALUE)
    public Map<String, Object> getBuildsByBoardName(@PathVariable("name") String name) {

        DashboardDTO dashboard = dashboardService.getDashboard(name);
        if (dashboard == null || dashboard.getCodeRepos() == null
                || dashboard.getCodeRepos().isEmpty()) {
            return null;
        }

        List<Build> builds = buildService
                .getLastBuildsByKeywordsAndByTeamMembers(
                        dashboard.getCodeRepos(), dashboard.getTeamMembers());
        BuildStats stats = buildService.getStatsByKeywordsAndByTeamMembers(
                dashboard.getCodeRepos(), dashboard.getTeamMembers());

        Map<String, Object> response = new HashMap<>();
        response.put("lastBuilds", builds);
        response.put("stats", stats);

        return response;
    }

    @RequestMapping(value = "/dashboards/{name}/builds/rate", method = GET,
        produces = APPLICATION_JSON_VALUE)
    public BuildStats getStats(@PathVariable("name") String name) {

        DashboardDTO dashboard = dashboardService.getDashboard(name);
        if (dashboard == null
                || dashboard.getCodeRepos() == null) {
            return null;
        }

        return buildService.getStatsByKeywordsAndByTeamMembers(
                dashboard.getCodeRepos(), dashboard.getTeamMembers());
    }

    @RequestMapping(value = "/api/builds", method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BuildDTO> createBuilds(@Valid @RequestBody BuildDTO request) {

        BuildDTO response = buildService.createOrUpdate(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}