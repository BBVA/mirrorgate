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
import static org.junit.Assert.assertTrue;

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


    @Autowired
    private HistoricUserMetricRepository repository;

    @Before
    public void init(){
        HistoricUserMetric userMetric1 = new HistoricUserMetric()
            .setViewId("AWS/Mirrorgate")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(12d)
            .setSampleSize(0d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric2 = new HistoricUserMetric()
            .setViewId("AWS/Mirrorgate")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(16d)
            .setSampleSize(0d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric3 = new HistoricUserMetric()
            .setViewId("AWS/Mirrorgate")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(14d)
            .setSampleSize(0d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric4 = new HistoricUserMetric()
            .setViewId("AWS/Mirrorgate")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("responseTime")
            .setIdentifier("AWSResponseTime")
            .setValue(3000d)
            .setSampleSize(100d)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric5 = new HistoricUserMetric()
            .setViewId("AWS/Mirrorgate")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("responseTime")
            .setIdentifier("AWSResponseTime")
            .setValue(2000d)
            .setSampleSize(150d)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric6 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(12d)
            .setSampleSize(0d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric7 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(16d)
            .setSampleSize(0d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric8 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(14d)
            .setSampleSize(0d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric9 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("responseTime")
            .setIdentifier("AWSResponseTime")
            .setValue(1500d)
            .setSampleSize(100d)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric10 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("responseTime")
            .setIdentifier("AWSResponseTime")
            .setValue(1500d)
            .setSampleSize(150d)
            .setTimestamp(TODAY);

        Iterable<HistoricUserMetric> minuteUserMetrics = Arrays
            .asList(userMetric1, userMetric2, userMetric3, userMetric4, userMetric5, userMetric6, userMetric7, userMetric8, userMetric9, userMetric10);

        repository.save(minuteUserMetrics);
    }

    @Test
    public void testOperationMetricsAverages(){
        List<HistoricUserMetricStats> result =
            repository.getUserMetricAverageTendencyForPeriod(Arrays.asList("ga:155019618", "AWS/Mirrorgate"),
                ChronoUnit.MINUTES,
                Arrays.asList("responseTime", "requestsNumber"),
                LocalDateTimeHelper.getTimestampForNUnitsAgo(10, ChronoUnit.MINUTES));

        List<HistoricUserMetricStats> responseTimeResult = result.stream()
                                                                        .filter(h -> h.getName().equalsIgnoreCase("responseTime"))
                                                                        .collect(Collectors.toList());

        assertTrue(result.size() == 2);
        assertTrue(responseTimeResult.size() == 1);
        assertTrue(responseTimeResult.get(0).getValue() == 16);
    }

    @Test
    public void testOperationMetricsSum(){
        List<HistoricUserMetricStats> result =
            repository.getUserMetricSumTotalForPeriod(Arrays.asList("ga:155019618", "AWS/Mirrorgate"),
                ChronoUnit.MINUTES,
                LocalDateTimeHelper.getTimestampForNUnitsAgo(10, ChronoUnit.MINUTES));

        List<HistoricUserMetricStats> requestsNumberResult = result.stream()
            .filter(h -> h.getName().equalsIgnoreCase("requestsNumber"))
            .collect(Collectors.toList());

        assertTrue(result.size() == 2);
        assertTrue(requestsNumberResult.size() == 1);
        assertTrue(requestsNumberResult.get(0).getValue() == 84);
        assertTrue(requestsNumberResult.get(0).getSampleSize() == 6);
    }


    @After
    public void clean(){
        repository.deleteAll();
    }

}
