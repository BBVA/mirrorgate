package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricTendenciesDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import java.util.List;
import java.util.Map;

public interface HistoricUserMetricService {

    void addToCurrentPeriod(Iterable<UserMetric> saved);

    Map<String, HistoricTendenciesDTO> getHistoricMetricsForDashboard(DashboardDTO dashboard, List<String> metricNames);
}
