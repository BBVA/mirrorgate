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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.UserMetricsDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.UserMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by alfonso on 27/07/17.
 */

@RestController
public class UserMetricsController {

    private UserMetricsService userMetricsService;
    private DashboardService dashboardService;

    @Autowired
    public UserMetricsController(UserMetricsService userMetricsService, DashboardService dashboardService) {
        this.userMetricsService = userMetricsService;
        this.dashboardService = dashboardService;
    }

    @RequestMapping(method = GET, value = "/api/user-metrics/analytic-views", produces = APPLICATION_JSON_VALUE)
    public List<String> getAnalyticViewIds() {
        return userMetricsService.getAnalyticViewIds();
    }

    @RequestMapping(method = POST, value ="/api/user-metrics", produces = APPLICATION_JSON_VALUE)
    public List<UserMetricsDTO> saveMetrics(@Valid @RequestBody Iterable<UserMetricsDTO> metrics) {
        return userMetricsService.saveMetrics(metrics);
    }

    @RequestMapping(method = GET, value ="/dashboards/{name}/user-metrics", produces = APPLICATION_JSON_VALUE)
    public List<UserMetricsDTO> getMetricsForBoard(@PathVariable("name") String name) {
        Dashboard dashboard = dashboardService.getDashboard(name);
        return userMetricsService.getMetricsFroDashboard(dashboard);
    }

}
