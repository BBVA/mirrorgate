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

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.service.BugService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author enrique
 */
@RestController
public class BugController {

    private final DashboardService dashboardService;
    private final BugService bugService;


    @Autowired
    public BugController(DashboardService dashboardService, BugService bugService) {
        this.dashboardService = dashboardService;
        this.bugService = bugService;
    }

    @RequestMapping(value = "/dashboards/{name}/bugs",
            method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBugs(@PathVariable("name") String name) {
        Dashboard dashboard = dashboardService.getDashboard(name);
        List<String> boards = dashboard.getBoards();
        return ResponseEntity.ok(bugService.getActiveBugsByBoards(boards));
    }

}
