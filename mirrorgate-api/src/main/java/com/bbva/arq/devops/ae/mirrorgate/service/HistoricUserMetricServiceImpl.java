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
import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper.getTimestampForNUnitsAgo;
import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper.getTimestampPeriod;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

import com.bbva.arq.devops.ae.mirrorgate.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.HistoricUserMetricMapper;
import com.bbva.arq.devops.ae.mirrorgate.mapper.UserMetricMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetricStats;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.UserMetricsRepository;
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

    private static final Logger LOG = LoggerFactory.getLogger(HistoricUserMetricServiceImpl.class);

    private static final int ONE_HOUR_PERIOD = 60; // MINUTES
    private static final int EIGHT_HOURS_PERIOD = 8; // HOURS
    private static final int ONE_DAY_PERIOD = 24; // HOURS
    private static final int SEVEN_DAYS_PERIOD = 7; // DAYS
    private static final int THIRTY_DAYS_PERIOD = 30; // DAYS
    private static final int NINETY_DAYS_PERIOD = 90; // DAYS

    private final UserMetricsRepository userMetricsRepository;
    private final HistoricUserMetricRepository historicUserMetricRepository;

    @Autowired
    public HistoricUserMetricServiceImpl(
        final UserMetricsRepository userMetricsRepository,
        final HistoricUserMetricRepository historicUserMetricRepository
    ) {
        this.userMetricsRepository = userMetricsRepository;
        this.historicUserMetricRepository = historicUserMetricRepository;
    }

    @Override
    public void addToCurrentPeriod(final Iterable<UserMetric> saved) {
        saved.forEach(s -> {
            addToTendency(s, ChronoUnit.MINUTES);
            addToTendency(s, ChronoUnit.HOURS);
            addToTendency(s, ChronoUnit.DAYS);
        });
    }

    @Override
    public List<UserMetricDTO> getHistoricMetrics(final List<String> views) {

        if (views == null || views.isEmpty()) {
            return null;
        }

        final List<UserMetricDTO> lastValueMetrics = userMetricsRepository
            .findAllStartingWithViewId(views)
            .stream()
            .map(UserMetricMapper::map)
            .collect(Collectors.toList());

        final Map<String, HistoricUserMetricStats> oneHourPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, HOURS, getTimestampForNUnitsAgo(ONE_HOUR_PERIOD, HOURS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        final Map<String, HistoricUserMetricStats> eightHoursPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, HOURS, getTimestampForNUnitsAgo(EIGHT_HOURS_PERIOD, HOURS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        final Map<String, HistoricUserMetricStats> oneDayPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, HOURS, getTimestampForNUnitsAgo(ONE_DAY_PERIOD, HOURS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        final Map<String, HistoricUserMetricStats> sevenDaysPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, DAYS, getTimestampForNUnitsAgo(SEVEN_DAYS_PERIOD, DAYS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        final Map<String, HistoricUserMetricStats> thirtyDaysPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, DAYS, getTimestampForNUnitsAgo(THIRTY_DAYS_PERIOD, DAYS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        final Map<String, HistoricUserMetricStats> ninetyDaysPeriodMetrics = historicUserMetricRepository
            .getUserMetricTendencyForPeriod(views, DAYS, getTimestampForNUnitsAgo(NINETY_DAYS_PERIOD, DAYS))
            .stream()
            .collect(Collectors.toMap(HistoricUserMetricStats::getIdentifier, HistoricUserMetricStats::getInstance));

        return lastValueMetrics
            .stream()
            .map(metric -> metric
                .setOneHourValue(oneHourPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : oneHourPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setOneHourSampleSize(oneHourPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : oneHourPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
                .setEightHoursValue(eightHoursPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : eightHoursPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setEightHoursSampleSize(eightHoursPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : eightHoursPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
                .setOneDayValue(oneDayPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : oneDayPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setOneDaySampleSize(oneDayPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : oneDayPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
                .setSevenDaysValue(sevenDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : sevenDaysPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setSevenDaysSampleSize(sevenDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : sevenDaysPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
                .setThirtyDaysValue(thirtyDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : thirtyDaysPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setThirtyDaysSampleSize(thirtyDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : thirtyDaysPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
                .setNinetyDaysValue(ninetyDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : ninetyDaysPeriodMetrics.get(metric.getIdentifier()).getValue())
                .setNinetyDaysSampleSize(ninetyDaysPeriodMetrics.get(metric.getIdentifier()) == null ? null
                    : ninetyDaysPeriodMetrics.get(metric.getIdentifier()).getSampleSize())
            )
            .collect(Collectors.toList());
    }

    private void addToTendency(final UserMetric userMetric, final ChronoUnit unit) {

        HistoricUserMetric historicUserMetric = historicUserMetricRepository.findById(
            HistoricUserMetricMapper.generateId(
                userMetric.getIdentifier(),
                unit,
                getTimestampPeriod(userMetric.getTimestamp(), unit)
            )
        );

        if (historicUserMetric == null) {
            historicUserMetric = createNextPeriod(userMetric, unit);
        }

        long metricSampleSize = 1;

        if (userMetric.getValue() != null) {
            if (userMetric.getSampleSize() != null && userMetric.getSampleSize() > 0) {
                metricSampleSize = userMetric.getSampleSize();
            }

            historicUserMetric
                .setValue(historicUserMetric.getValue() + (userMetric.getValue() * metricSampleSize))
                .setSampleSize(historicUserMetric.getSampleSize() + metricSampleSize);
        }

        historicUserMetricRepository.save(historicUserMetric);

    }

    private HistoricUserMetric createNextPeriod(final UserMetric userMetric, final ChronoUnit unit) {

        LOG.debug("Creating new Historic Metric Period");

        return mapToHistoric(userMetric, unit, getTimestampPeriod(userMetric.getTimestamp(), unit))
            .setSampleSize(0L)
            .setValue(0d);
    }

}
