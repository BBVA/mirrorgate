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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.UserMetricsDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.UserMetricsMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetrics;
import com.bbva.arq.devops.ae.mirrorgate.repository.UserMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by alfonso on 27/07/17.
 */

@Component
public class UserMetricsServiceImpl implements UserMetricsService {

    private DashboardService dashboardService;
    private UserMetricsRepository userMetricsRepository;

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
    public List<UserMetricsDTO> saveMetrics(Iterable<UserMetricsDTO> metrics) {
        List<UserMetrics> targets = userMetricsRepository.findAllByViewIdIn(
                StreamSupport.stream(metrics.spliterator(), false)
                        .map(UserMetricsDTO::getViewId)
                        .collect(Collectors.toList())
        );
        List<UserMetrics> toSave = StreamSupport.stream(metrics.spliterator(), false)
                .map((metric) -> {

            Optional<UserMetrics> optTarget = targets.stream()
                    .filter((t) -> metric.getViewId().equals(t.getViewId()))
                    .findAny();

            return UserMetricsMapper.map(
                    metric,
                    optTarget.isPresent() ? optTarget.get() : new UserMetrics()
                    );
        }).collect(Collectors.toList());

        userMetricsRepository.save(toSave);

        return toSave.stream().map(UserMetricsMapper::map).collect(Collectors.toList());
    }

    @Override
    public List<UserMetricsDTO> getMetricsForDashboard(Dashboard dashboard) {
        List<String> views = dashboard.getAnalyticViews();
        if(views == null || views.size() == 0) {
            return new ArrayList<>();
        }

        return userMetricsRepository.findAllByViewIdIn(views)
                .stream().map(UserMetricsMapper::map)
                .collect(Collectors.toList());
    }


}
