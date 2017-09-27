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

package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.UserMetricMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.UserMetricsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMetricsServiceImpl implements UserMetricsService {

    private final DashboardService dashboardService;
    private final UserMetricsRepository userMetricsRepository;

    @Autowired
    public UserMetricsServiceImpl(DashboardService dashboardService, UserMetricsRepository userMetricsRepository){
        this.dashboardService = dashboardService;
        this.userMetricsRepository = userMetricsRepository;
    }

    @Override
    public List<String> getAnalyticViewIds() {
        return dashboardService.getActiveDashboards().stream()
                .flatMap((d) -> d.getAnalyticViews() == null ?
                        Stream.empty() :
                        d.getAnalyticViews().stream()
                )
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<UserMetricDTO> getMetricsByCollectorId(String collectorId) {
        return userMetricsRepository.findAllByCollectorId(collectorId).stream()
                .map(UserMetricMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<UserMetricDTO> saveMetrics(Iterable<UserMetricDTO> metrics) {
        List<UserMetric> targets = userMetricsRepository.findAllByViewIdIn(
                StreamSupport.stream(metrics.spliterator(), false)
                .map(UserMetricDTO::getViewId)
                .filter((e) -> e != null)
                .distinct()
                .collect(Collectors.toList())
        );
        List<UserMetric> toSave = StreamSupport.stream(metrics.spliterator(), false)
                .map((metric) -> {

            Optional<UserMetric> optTarget = targets.stream()
                    .filter((t) -> t.isTheSame(metric))
                    .findAny();

            return UserMetricMapper.map(
                    metric,
                    optTarget.isPresent() ? optTarget.get() : new UserMetric()                    );
        }).collect(Collectors.toList());

        userMetricsRepository.save(toSave);

        return toSave.stream().map(UserMetricMapper::map).collect(Collectors.toList());

    }

    @Override
    public List<UserMetricDTO> getMetricsForDashboard(Dashboard dashboard) {
        List<String> views = dashboard.getAnalyticViews();
        if (views == null || views.isEmpty()) {
            return new ArrayList<>();
        }

        return userMetricsRepository.findAllByViewIdIn(views)
                .stream().map(UserMetricMapper::map)
                .collect(Collectors.toList());
    }

}
