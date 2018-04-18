package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import java.util.List;

public interface HistoricUserMetricService {

    void addToCurrentPeriod(Iterable<UserMetric> saved);

    List<UserMetricDTO> getHistoricMetrics(List<String> views);
}
