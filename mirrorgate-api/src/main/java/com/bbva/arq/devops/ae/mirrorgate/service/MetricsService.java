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
import java.util.List;

public interface MetricsService {

    List<String> getAnalyticViewIds();

    List<UserMetricDTO> getMetricsByCollectorId(String collectorId);

    List<UserMetricDTO> saveMetrics(Iterable<UserMetricDTO> metrics);

    List<UserMetricDTO> getMetricsForDashboard(DashboardDTO dashboard);

}
