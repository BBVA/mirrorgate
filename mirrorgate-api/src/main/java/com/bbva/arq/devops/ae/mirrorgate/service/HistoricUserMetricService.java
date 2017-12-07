package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;

public interface HistoricUserMetricService {

    HistoricUserMetric createNextPeriod(UserMetric userMetric);
    HistoricUserMetric getHistoricMetricForPeriod(long periodTimestamp, String identifier);
    void getLastNPeriods(int n);
    void addToCurrentPeriod(Iterable<UserMetric> saved);
    void removePeriod();
}
