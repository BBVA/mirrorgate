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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricTendenciesDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.UserMetricMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.UserMetricsRepository;
import com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
    private final HistoricUserMetricRepository historicUserMetricRepository;

    private static final int METRIC_MINUTES_PERIOD = 10;

    @Autowired
    public MetricsServiceImpl(DashboardService dashboardService, UserMetricsRepository userMetricsRepository, HistoricUserMetricService historicUserMetricService, HistoricUserMetricRepository historicUserMetricRepository) {
        this.dashboardService = dashboardService;
        this.userMetricsRepository = userMetricsRepository;
        this.historicUserMetricService = historicUserMetricService;
        this.historicUserMetricRepository = historicUserMetricRepository;
    }

    @Override
    public List<String> getAnalyticViewIds() {
        return dashboardService.getActiveDashboards().stream()
            .flatMap((d) -> {
                List<String> analyticsViews = new ArrayList<>();
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
    public List<UserMetricDTO> getMetricsByCollectorId(String collectorId) {
        return userMetricsRepository.findAllByCollectorId(collectorId).stream()
                .map(UserMetricMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<UserMetricDTO> saveMetrics(Iterable<UserMetricDTO> metrics) {
        List<UserMetric> toSave = StreamSupport.stream(metrics.spliterator(), false)
                .map(UserMetricMapper::map)
                .collect(Collectors.toList());

        Iterable<UserMetric> saved = userMetricsRepository.save(toSave);

        //send to historic
        historicUserMetricService.addToCurrentPeriod(saved);

        return toSave.stream().map(UserMetricMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<UserMetricDTO> getMetricsForDashboard(DashboardDTO dashboard) {
        List<String> views = Stream.of(dashboard.getAnalyticViews(), dashboard.getOperationViews())
            .filter(v -> v != null)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        if(views.isEmpty()){
            return new ArrayList<>();
        }

        List<UserMetricDTO> userMetrics = userMetricsRepository
            .findAllStartingWithViewIdWithNon0Values(views)
            .stream()
            .map(UserMetricMapper::map)
            .map((metric) -> metric.setLastValue(metric.getValue()))
            .collect(Collectors.toList());

        List<UserMetricDTO> operationMetrics = historicUserMetricRepository
            .getUserMetricSumTotalForPeriod(views, ChronoUnit.MINUTES, LocalDateTimeHelper.getTimestampForNUnitsAgo(METRIC_MINUTES_PERIOD, ChronoUnit.MINUTES))
            .stream()
            .map(UserMetricMapper::map)
            .collect(Collectors.toList());

        List<UserMetricDTO> metrics = Stream.of(userMetrics, operationMetrics).flatMap(Collection::stream)
            .collect(Collectors.toList());

        List<String> metricNames = metrics.stream().map(UserMetricDTO::getName).distinct().collect(Collectors.toList());

        Map<String, HistoricTendenciesDTO> historicUserMetrics = historicUserMetricService.getHistoricMetricsForDashboard(dashboard, metricNames);

        return historicUserMetrics != null ? metrics.stream()
                .map(u -> {
                        u.setLongTermTendency(historicUserMetrics.get(u.getName()).getLongTermTendency());
                        u.setMidTermTendency(historicUserMetrics.get(u.getName()).getMidTermTendency());
                        u.setShortTermTendency(historicUserMetrics.get(u.getName()).getShortTermTendency());

                        return u;
                    }
                ).collect(Collectors.toList()) : metrics;
    }

}
