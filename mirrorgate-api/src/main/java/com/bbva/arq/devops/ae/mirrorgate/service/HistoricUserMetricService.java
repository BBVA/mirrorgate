package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricTendenciesDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricUserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import java.time.temporal.ChronoUnit;
import java.util.List;

public interface HistoricUserMetricService {

    void removeExtraPeriodsForMetricAndIdentifier(String metricName, String identifier, ChronoUnit unit, long timestamp);

    void addToCurrentPeriod(Iterable<UserMetric> saved);

    HistoricUserMetric getHistoricMetricForPeriod(long periodTimestamp, String identifier, ChronoUnit type);

    HistoricTendenciesDTO getHistoricMetricsForDashboard(DashboardDTO dashboard, String metricName);
}
