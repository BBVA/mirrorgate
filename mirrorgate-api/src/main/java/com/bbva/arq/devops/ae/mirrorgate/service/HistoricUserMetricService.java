package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import java.util.List;

public interface HistoricUserMetricService {

    HistoricUserMetric createNextPeriod(UserMetric userMetric);

    HistoricUserMetric getHistoricMetricForPeriod(long periodTimestamp, String identifier);

    List<HistoricUserMetric> getLastNPeriods(int n, String metricName, String identifier);

    void addToCurrentPeriod(Iterable<UserMetric> saved);

    void removeExtraPeriodsForMetricAndIdentifier(int daysToKeep, String metricName, String identifier);
}
