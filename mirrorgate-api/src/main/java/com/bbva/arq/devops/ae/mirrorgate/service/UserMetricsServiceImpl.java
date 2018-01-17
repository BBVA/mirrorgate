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
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.UserMetricsRepository;
import com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class UserMetricsServiceImpl implements UserMetricsService {

    private final DashboardService dashboardService;
    private final UserMetricsRepository userMetricsRepository;
    private final HistoricUserMetricService historicUserMetricService;
    private final HistoricUserMetricRepository historicUserMetricRepository;

    @Autowired
    public UserMetricsServiceImpl(DashboardService dashboardService, UserMetricsRepository userMetricsRepository, HistoricUserMetricService historicUserMetricService, HistoricUserMetricRepository historicUserMetricRepository) {
        this.dashboardService = dashboardService;
        this.userMetricsRepository = userMetricsRepository;
        this.historicUserMetricService = historicUserMetricService;
        this.historicUserMetricRepository = historicUserMetricRepository;
    }

    @Override
    public List<String> getAnalyticViewIds() {
        return dashboardService.getActiveDashboards().stream()
                .flatMap((d) -> d.getAnalyticViews() == null
                        ? Stream.empty()
                        : d.getAnalyticViews().stream()
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
        List<String> views = dashboard.getAnalyticViews();

        if (views == null || views.isEmpty()) {
            return new ArrayList<>();
        }

        List<UserMetricDTO> userMetrics = historicUserMetricRepository.findAllByViewIdInAndHistoricTypeAndTimestampGreaterThanEqual(views, ChronoUnit.MINUTES, LocalDateTimeHelper.getTimestampForNMinutesAgo(10, ChronoUnit.MINUTES))
            .stream()
            .map(UserMetricMapper::map)
            .collect(Collectors.toList());

        List<String> metricNames = userMetrics.stream().map(UserMetricDTO::getName).distinct().collect(Collectors.toList());

        Map<String, HistoricTendenciesDTO> historicUserMetrics = historicUserMetricService.getHistoricMetricsForDashboard(dashboard, metricNames);

        return userMetrics.stream()
                .map(u -> {
                        u.setLongTermTendency(historicUserMetrics.get(u.getName()).getLongTermTendency());
                        u.setShortTermTendency(historicUserMetrics.get(u.getName()).getShortTermTendency());

                        return u;
                    }
                ).collect(Collectors.toList());
    }
}
