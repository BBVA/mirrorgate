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

import static com.bbva.arq.devops.ae.mirrorgate.mapper.HistoricUserMetricMapper.mapToHistoric;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricTendenciesDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetricStats;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HistoricUserMetricServiceImpl implements HistoricUserMetricService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricUserMetricServiceImpl.class);

    private static final int LONG_TERM_TENDENCY_LONG_PERIOD = 30;
    private static final int LONG_TERM_TENDENCY_SHORT_PERIOD = 4;
    private static final int MID_TERM_TENDENCY_LONG_PERIOD = 24;
    private static final int MID_TERM_TENDENCY_SHORT_PERIOD = 2;
    private static final int SHORT_TERM_TENDENCY_LONG_PERIOD = 8;
    private static final int SHORT_TERM_TENDENCY_SHORT_PERIOD = 1;

    private final HistoricUserMetricRepository historicUserMetricRepository;

    @Autowired
    public HistoricUserMetricServiceImpl(HistoricUserMetricRepository historicUserMetricRepository){

        this.historicUserMetricRepository = historicUserMetricRepository;
    }


    @Override
    public void addToCurrentPeriod(Iterable<UserMetric> saved) {

        saved.forEach( s -> {
            try {
                addToTendency(s, ChronoUnit.MINUTES);
                addToTendency(s, ChronoUnit.HOURS);
                addToTendency(s, ChronoUnit.DAYS);
            } catch (Exception e) {
                LOGGER.error("Error while processing metric : {}", s.getName(), e);
            }
        });
    }

    @Override
    public Map<String, HistoricTendenciesDTO> getHistoricMetricsForDashboard(DashboardDTO dashboard, List<String> metricNames) {

        List<String> views = dashboard.getAnalyticViews();

        if (views == null || views.isEmpty()) {
            return null;
        }

        Map<String, Double> longTermTendency = calculateTendency(views, metricNames, ChronoUnit.DAYS, LONG_TERM_TENDENCY_LONG_PERIOD, LONG_TERM_TENDENCY_SHORT_PERIOD);
        Map<String, Double> midTermTendency = calculateTendency(views, metricNames, ChronoUnit.HOURS, MID_TERM_TENDENCY_LONG_PERIOD, MID_TERM_TENDENCY_SHORT_PERIOD);
        Map<String, Double> shortTermTendency = calculateTendency(views, metricNames, ChronoUnit.HOURS, SHORT_TERM_TENDENCY_LONG_PERIOD, SHORT_TERM_TENDENCY_SHORT_PERIOD);

        return metricNames.stream()
                .collect(Collectors.toMap(s -> s, s -> new HistoricTendenciesDTO(longTermTendency.get(s) == null ? 0 : longTermTendency.get(s)
                                                                                , midTermTendency.get(s) == null ? 0 : midTermTendency.get(s)
                                                                                , shortTermTendency.get(s) == null ? 0 : shortTermTendency.get(s))));
    }

    private HistoricUserMetric getHistoricMetricForPeriod(long periodTimestamp, String identifier, ChronoUnit type) {

        return historicUserMetricRepository.findByTimestampAndIdentifierAndHistoricType(periodTimestamp, identifier, type);
    }

    private HistoricUserMetric addToTendency(UserMetric userMetric, ChronoUnit unit) {

        HistoricUserMetric metric = getHistoricMetricForPeriod(
            LocalDateTimeHelper.getTimestampPeriod(userMetric.getTimestamp(), unit),
            userMetric.getId(), unit);

        if (metric == null) {
            metric = createNextPeriod(userMetric, unit);
        }

        HistoricUserMetric addedMetric = addMetrics(metric, userMetric);
        historicUserMetricRepository.save(addedMetric);

        return metric;
    }

    private HistoricUserMetric addMetrics (final HistoricUserMetric historic, final UserMetric saved){

        HistoricUserMetric response =  historic;
        double metricSampleSize = 1;

        if(saved.getValue() != null) {
            if(saved.getSampleSize() != null && saved.getSampleSize() > 0){
                metricSampleSize = saved.getSampleSize();
            }

            response.setValue(historic.getValue() + (saved.getValue() * metricSampleSize));
            response.setSampleSize(historic.getSampleSize() + metricSampleSize);
        }

        return response;
    }

    private HistoricUserMetric createNextPeriod(UserMetric userMetric, ChronoUnit unit) {

        LOGGER.debug("creating new Historic Metric Period");

        HistoricUserMetric historicUserMetric = mapToHistoric(userMetric);

        historicUserMetric.setSampleSize(0d);
        historicUserMetric.setTimestamp(LocalDateTimeHelper.getTimestampPeriod(userMetric.getTimestamp(), unit));
        historicUserMetric.setValue(0d);
        historicUserMetric.setHistoricType(unit);

        return historicUserMetric;
    }

    private Map<String, Double> calculateTendency(List<String> views, List<String> metricNames, ChronoUnit unit, int longPeriod, int shortPeriod){

        List<HistoricUserMetricStats> longPeriodHistoricUserMetrics =
            historicUserMetricRepository.getUserMetricAverageTendencyForPeriod(views, unit, metricNames, LocalDateTimeHelper.getTimestampForNUnitsAgo(longPeriod, unit));

        List<HistoricUserMetricStats> shortPeriodHistoricUserMetrics =
            historicUserMetricRepository.getUserMetricAverageTendencyForPeriod(views, unit, metricNames, LocalDateTimeHelper.getTimestampForNUnitsAgo(shortPeriod, unit));

        Map<String, Double> longPeriodMap = longPeriodHistoricUserMetrics.stream().collect(
            Collectors.toMap(
                HistoricUserMetricStats::getName, HistoricUserMetricStats::getValue));

        Map<String, Double> shortPeriodMap = shortPeriodHistoricUserMetrics.stream().collect(
            Collectors.toMap(
                HistoricUserMetricStats::getName, HistoricUserMetricStats::getValue));

        return longPeriodMap.keySet()
            .stream()
            .collect(Collectors.toMap(s -> s, s -> getPercentualDifference(longPeriodMap.get(s), shortPeriodMap.get(s) == null ? 0 : shortPeriodMap.get(s))));
    }

    private double getPercentualDifference(double longPeriod, double shortPeriod){

        return longPeriod != 0 ? ((shortPeriod - longPeriod)/longPeriod) * 100 : 0;
    }
}
