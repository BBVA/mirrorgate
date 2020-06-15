package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import java.util.List;

interface HistoricUserMetricService {

    void addToCurrentPeriod(final Iterable<UserMetric> saved);

    List<UserMetricDTO> getHistoricMetrics(final List<String> views);
}
