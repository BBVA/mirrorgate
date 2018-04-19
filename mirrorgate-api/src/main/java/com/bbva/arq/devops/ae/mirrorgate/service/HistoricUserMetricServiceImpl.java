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
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

import com.bbva.arq.devops.ae.mirrorgate.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.UserMetricMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetricStats;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.UserMetricsRepository;
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

    private static final int ONE_HOUR_PERIOD = 60; // MINUTES
    private static final int EIGHT_HOURS_PERIOD = 8; // HOURS
    private static final int ONE_DAY_PERIOD = 24; // HOURS
    private static final int SEVEN_DAYS_PERIOD = 7; // DAYS
    private static final int THIRTY_DAYS_PERIOD = 30; // DAYS
    private static final int NINETY_DAYS_PERIOD = 90; // DAYS

    private final UserMetricsRepository userMetricsRepository;
    private final HistoricUserMetricRepository historicUserMetricRepository;

    @Autowired
    public HistoricUserMetricServiceImpl(UserMetricsRepository userMetricsRepository, HistoricUserMetricRepository historicUserMetricRepository) {
        this.userMetricsRepository = userMetricsRepository;
        this.historicUserMetricRepository = historicUserMetricRepository;
    }

    @Override
    public void addToCurrentPeriod(Iterable<UserMetric> saved) {

        saved.forEach(s -> {
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
    public List<UserMetricDTO> getHistoricMetrics(List<String> views) {

        if (views == null || views.isEmpty()) {
            return null;
        }

        List<UserMetricDTO> lastValueMetrics = userMetricsRepository
            .findAllStartingWithViewId(views)
            .stream()
            .map(UserMetricMapper::map)
            .collect(Collectors.toList());

        Map<String, HistoricUserMetricStats> oneHourPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, HOURS, LocalDateTimeHelper.getTimestampForNUnitsAgo(ONE_HOUR_PERIOD, HOURS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        Map<String, HistoricUserMetricStats> eightHoursPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, HOURS, LocalDateTimeHelper.getTimestampForNUnitsAgo(EIGHT_HOURS_PERIOD, HOURS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        Map<String, HistoricUserMetricStats> oneDayPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, HOURS, LocalDateTimeHelper.getTimestampForNUnitsAgo(ONE_DAY_PERIOD, HOURS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        Map<String, HistoricUserMetricStats> sevenDaysPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, DAYS, LocalDateTimeHelper.getTimestampForNUnitsAgo(SEVEN_DAYS_PERIOD, DAYS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        Map<String, HistoricUserMetricStats> thirtyDaysPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, DAYS, LocalDateTimeHelper.getTimestampForNUnitsAgo(THIRTY_DAYS_PERIOD, DAYS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        Map<String, HistoricUserMetricStats> ninetyDaysPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, DAYS, LocalDateTimeHelper.getTimestampForNUnitsAgo(NINETY_DAYS_PERIOD, DAYS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        return lastValueMetrics
            .stream()
            .map(metric -> metric
                .setOneHourValue(oneHourPeriodMetrics.get(metric.getIdentifier()) == null ? null : oneHourPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setOneHourSampleSize(oneHourPeriodMetrics.get(metric.getIdentifier()) == null ? null : oneHourPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
                .setEightHoursValue(eightHoursPeriodMetrics.get(metric.getIdentifier()) == null ? null : eightHoursPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setEightHoursSampleSize(eightHoursPeriodMetrics.get(metric.getIdentifier()) == null ? null : eightHoursPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
                .setOneDayValue(oneDayPeriodMetrics.get(metric.getIdentifier()) == null ? null : oneDayPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setOneDaySampleSize(oneDayPeriodMetrics.get(metric.getIdentifier()) == null ? null : oneDayPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
                .setSevenDaysValue(sevenDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null : sevenDaysPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setSevenDaysSampleSize(sevenDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null : sevenDaysPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
                .setThirtyDaysValue(thirtyDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null : thirtyDaysPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setThirtyDaysSampleSize(thirtyDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null : thirtyDaysPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
                .setNinetyDaysValue(ninetyDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null : ninetyDaysPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setNinetyDaysSampleSize(ninetyDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null : ninetyDaysPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
            )
            .collect(Collectors.toList());
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

    private HistoricUserMetric addMetrics(final HistoricUserMetric historic, final UserMetric saved) {

        HistoricUserMetric response = historic;
        long metricSampleSize = 1;

        if (saved.getValue() != null) {
            if (saved.getSampleSize() != null && saved.getSampleSize() > 0) {
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

        historicUserMetric.setSampleSize(0L);
        historicUserMetric.setTimestamp(LocalDateTimeHelper.getTimestampPeriod(userMetric.getTimestamp(), unit));
        historicUserMetric.setValue(0d);
        historicUserMetric.setHistoricType(unit);

        return historicUserMetric;
    }

}
