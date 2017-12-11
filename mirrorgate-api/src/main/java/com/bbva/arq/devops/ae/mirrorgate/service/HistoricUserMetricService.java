package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricUserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import java.util.List;

public interface HistoricUserMetricService {

    HistoricUserMetric createNextPeriod(UserMetric userMetric);

    HistoricUserMetric getHistoricMetricForPeriod(long periodTimestamp, String identifier);

    void addToCurrentPeriod(Iterable<UserMetric> saved);

    void removeExtraPeriodsForMetricAndIdentifier(int daysToKeep, String metricName, String identifier);

    List<HistoricUserMetricDTO> getHistoricMetricsForDashboard(DashboardDTO dashboard, String metricName, int numberOfResults);
}
