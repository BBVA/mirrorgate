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

import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.IssueStats;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.IssueService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IssueController {

    private final DashboardService dashboardService;
    private final IssueService issueService;

    @Autowired
    public IssueController(final DashboardService dashboardService, final IssueService issueService) {
        this.dashboardService = dashboardService;
        this.issueService = issueService;
    }

    @RequestMapping(value = "/dashboards/{name}/stories", method = GET, produces = APPLICATION_JSON_VALUE)
    public Map<String, Object> getActiveUserStories(final @PathVariable("name") String name) {
        final DashboardDTO dashboard = dashboardService.getDashboard(name);

        final List<String> boards = dashboard.getBoards();

        final Map<String, Object> response = new HashMap<>();

        response.put("currentSprint", issueService.getActiveUserStoriesByBoards(boards));
        response.put("stats", getStoriesStats(name));
        return response;
    }


    @RequestMapping(value = "/dashboards/{name}/stories/_stats", method = GET, produces = APPLICATION_JSON_VALUE)
    public IssueStats getStoriesStats(final @PathVariable("name") String name) {
        final DashboardDTO dashboard = dashboardService.getDashboard(name);

        final List<String> boards = dashboard.getBoards();
        return issueService.getIssueStatsByKeywords(boards);
    }

    @RequestMapping(value = "/api/issues", method = POST, produces = APPLICATION_JSON_VALUE)
    public Iterable<IssueDTO> saveOrUpdateIssues(
        final @Valid @RequestBody List<IssueDTO> issues,
        final @RequestParam("collectorId") String collectorId
    ) {
        return issueService.saveOrUpdateStories(issues, collectorId);
    }

    @RequestMapping(value = "/api/issues/{id}", method = DELETE, produces = APPLICATION_JSON_VALUE)
    public IssueDTO deleteStory(
        final @PathVariable("id") String id,
        final @RequestParam("collectorId") String collectorId
    ) {
        return issueService.deleteStory(id, collectorId);
    }

}
