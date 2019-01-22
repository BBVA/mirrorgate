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

package com.bbva.arq.devops.ae.mirrorgate.repository;

import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.TODAY;
import static org.junit.Assert.assertEquals;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetricStats;
import com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataMongoTest
public class HistoricUserMetricRepositoryTest {

    private static final int METRIC_MINUTES_PERIOD = 10;

    @Autowired
    private HistoricUserMetricRepository repository;

    @Before
    public void init(){
        HistoricUserMetric userMetric1 = new HistoricUserMetric()
            .setIdentifier("requestsNumber1")
            .setViewId("AWS/Mirrorgate")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(12d)
            .setSampleSize(0L)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric2 = new HistoricUserMetric()
            .setIdentifier("requestsNumber2")
            .setViewId("AWS/Mirrorgate")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(16d)
            .setSampleSize(0L)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric3 = new HistoricUserMetric()
            .setIdentifier("requestsNumber3")
            .setViewId("AWS/Mirrorgate")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(14d)
            .setSampleSize(0L)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric4 = new HistoricUserMetric()
            .setIdentifier("responseTime4")
            .setViewId("AWS/Mirrorgate")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("responseTime")
            .setValue(3000d)
            .setSampleSize(100L)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric5 = new HistoricUserMetric()
            .setIdentifier("responseTime5")
            .setViewId("AWS/Mirrorgate")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("responseTime")
            .setValue(2000d)
            .setSampleSize(150L)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric6 = new HistoricUserMetric()
            .setIdentifier("requestsNumber6")
            .setViewId("ga:0000")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(12d)
            .setSampleSize(0L)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric7 = new HistoricUserMetric()
            .setIdentifier("requestsNumber7")
            .setViewId("ga:0000")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(16d)
            .setSampleSize(0L)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric8 = new HistoricUserMetric()
            .setIdentifier("requestsNumber8")
            .setViewId("ga:0000")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(14d)
            .setSampleSize(0L)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric9 = new HistoricUserMetric()
            .setIdentifier("responseTime9")
            .setViewId("ga:0000")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("responseTime")
            .setValue(1500d)
            .setSampleSize(100L)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric10 = new HistoricUserMetric()
            .setIdentifier("responseTime10")
            .setViewId("ga:0000")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("responseTime")
            .setValue(1500d)
            .setSampleSize(150L)
            .setTimestamp(TODAY);

        Iterable<HistoricUserMetric> minuteUserMetrics = Arrays
            .asList(userMetric1, userMetric2, userMetric3, userMetric4, userMetric5, userMetric6, userMetric7, userMetric8, userMetric9, userMetric10);

        repository.saveAll(minuteUserMetrics);
    }

    @Test
    public void testOperationMetricsAverages(){
        List<HistoricUserMetricStats> result = repository
            .getUserMetricTendencyForPeriod(Arrays.asList("ga:0000", "AWS/Mirrorgate"), ChronoUnit.MINUTES, LocalDateTimeHelper.getTimestampForNUnitsAgo(METRIC_MINUTES_PERIOD, ChronoUnit.MINUTES));

        List<HistoricUserMetricStats> responseTimeResult = result
            .stream()
            .filter(metric -> metric.getName().contains("responseTime"))
            .collect(Collectors.toList());

        assertEquals(10, result.size());
        assertEquals(4, responseTimeResult.size());
        assertEquals(1500.0, responseTimeResult.get(0).getValue(), 0.0);
    }

    @After
    public void clean(){
        repository.deleteAll();
    }

}
