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

package com.bbva.arq.devops.ae.mirrorgate.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.UserMetricsService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserMetricsController {

    private final UserMetricsService userMetricsService;
    private final DashboardService dashboardService;

    @Autowired
    public UserMetricsController(UserMetricsService userMetricsService, DashboardService dashboardService) {
        this.userMetricsService = userMetricsService;
        this.dashboardService = dashboardService;
    }

    @RequestMapping(method = GET, value = "/api/user-metrics/analytic-views", produces = APPLICATION_JSON_VALUE)
    public List<String> getAnalyticViewIds() {
        return userMetricsService.getAnalyticViewIds();
    }

    @RequestMapping(
            method = GET,
            value = "/api/user-metrics",
            produces = APPLICATION_JSON_VALUE)
    public List<UserMetricDTO> getAnalyticViewIdsByCollectorId(
            @RequestParam("collectorId") String collectorId) {
        return userMetricsService.getMetricsByCollectorId(collectorId);
    }

    @RequestMapping(method = POST, value ="/api/user-metrics", produces = APPLICATION_JSON_VALUE)
    public List<UserMetricDTO> saveMetrics(@Valid @RequestBody Iterable<UserMetricDTO> metrics) {
        return userMetricsService.saveMetrics(metrics);
    }

    @RequestMapping(method = GET, value ="/dashboards/{name}/user-metrics", produces = APPLICATION_JSON_VALUE)
    public List<UserMetricDTO> getMetricsForBoard(@PathVariable("name") String name) {
        DashboardDTO dashboard = dashboardService.getDashboard(name);
        return userMetricsService.getMetricsForDashboard(dashboard);
    }

}
