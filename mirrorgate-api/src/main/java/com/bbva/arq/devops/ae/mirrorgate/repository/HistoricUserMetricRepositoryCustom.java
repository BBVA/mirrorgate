package com.bbva.arq.devops.ae.mirrorgate.repository;

import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepositoryImpl.HistoricUserMetricStats;
import java.util.List;

public interface HistoricUserMetricRepositoryCustom {

    List<HistoricUserMetricStats> getUserMetricAverageTendencyForPeriod(List<String> views, List<String> metricNames, long limitTimestamp);
}
