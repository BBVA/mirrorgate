package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricTendenciesDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricUserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public interface HistoricUserMetricService {

    void addToCurrentPeriod(Iterable<UserMetric> saved);

    HistoricUserMetric getHistoricMetricForPeriod(long periodTimestamp, String identifier, ChronoUnit type);

    Map<String, HistoricTendenciesDTO> getHistoricMetricsForDashboard(DashboardDTO dashboard, List<String> metricNames);
}
