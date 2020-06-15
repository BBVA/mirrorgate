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

import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.UserMetricMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.UserMetricsRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetricsServiceImpl implements MetricsService {

    private final DashboardService dashboardService;
    private final UserMetricsRepository userMetricsRepository;
    private final HistoricUserMetricService historicUserMetricService;

    @Autowired
    public MetricsServiceImpl(
        final DashboardService dashboardService,
        final UserMetricsRepository userMetricsRepository,
        final HistoricUserMetricService historicUserMetricService
    ) {
        this.dashboardService = dashboardService;
        this.userMetricsRepository = userMetricsRepository;
        this.historicUserMetricService = historicUserMetricService;
    }

    @Override
    public List<String> getAnalyticViewIds() {
        return dashboardService.getActiveDashboards().stream()
            .flatMap((d) -> {
                final List<String> analyticsViews = new ArrayList<>();
                if (d.getAnalyticViews() != null) {
                    analyticsViews.addAll(d.getAnalyticViews());
                }
                if (d.getOperationViews() != null) {
                    analyticsViews.addAll(d.getOperationViews());
                }
                return analyticsViews.stream();
            })
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public List<UserMetricDTO> getMetricsByCollectorId(final String collectorId) {
        return userMetricsRepository.findAllByCollectorId(collectorId).stream()
            .map(UserMetricMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<UserMetricDTO> saveMetrics(final Iterable<UserMetricDTO> metrics) {
        final List<UserMetric> toSave = StreamSupport.stream(metrics.spliterator(), false)
            .map(UserMetricMapper::map)
            .collect(Collectors.toList());

        final Iterable<UserMetric> saved = userMetricsRepository.saveAll(toSave);

        //send to historic
        historicUserMetricService.addToCurrentPeriod(saved);

        return toSave.stream().map(UserMetricMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<UserMetricDTO> getMetricsForDashboard(final DashboardDTO dashboard) {
        final List<String> views = Stream.of(dashboard.getAnalyticViews(), dashboard.getOperationViews())
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        if (views.isEmpty()) {
            return new ArrayList<>();
        }

        return historicUserMetricService.getHistoricMetrics(views);
    }

}
