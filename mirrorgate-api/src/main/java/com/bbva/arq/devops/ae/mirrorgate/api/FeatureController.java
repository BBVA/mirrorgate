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
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.FeatureStats;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.FeatureService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * Defines feature rest methods
 */
@RestController
public class FeatureController {

    private final DashboardService dashboardService;
    private final FeatureService featureService;

    @Autowired
    public FeatureController(DashboardService dashboardService, FeatureService featureService) {
        this.dashboardService = dashboardService;
        this.featureService = featureService;
    }

    @RequestMapping(value = "/dashboards/{name}/stories", method = GET, produces = APPLICATION_JSON_VALUE)
    public Map<String, Object> getAtiveUserStories(@PathVariable("name") String name) {
        Dashboard dashboard = dashboardService.getDashboard(name);
        List<String> boards = dashboard.getBoards();

        Map<String, Object> response = new HashMap<>();

        response.put("currentSprint", featureService.getActiveUserStoriesByBoards(boards));
        response.put("stats", getStoriesStats(name));
        return response;
    }


    @RequestMapping(value = "/dashboards/{name}/stories/_stats", method = GET, produces = APPLICATION_JSON_VALUE)
    public FeatureStats getStoriesStats(@PathVariable("name") String name) {
        Dashboard dashboard = dashboardService.getDashboard(name);
        List<String> boards = dashboard.getBoards();
        return featureService.getFeatureStatsByKeywords(boards);
    }

    @RequestMapping(value = "/api/issues", method = POST, produces = APPLICATION_JSON_VALUE)
    public Iterable<IssueDTO> saveOrUpdateIssues(@Valid @RequestBody List<IssueDTO> issues) {
        return featureService.saveOrUpdateStories(issues);
    }

    @RequestMapping(value = "/api/issues/{id}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public String saveOrUpdateIssues(@PathVariable("id") Long id) {
        featureService.deleteStory(id);
        return "ok";
    }

}
