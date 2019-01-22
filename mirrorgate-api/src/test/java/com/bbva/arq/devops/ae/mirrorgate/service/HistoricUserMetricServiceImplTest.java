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

import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.*;
import static org.junit.Assert.assertEquals;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class HistoricUserMetricServiceImplTest {

    @Autowired
    private HistoricUserMetricRepository repository;

    @Autowired
    private HistoricUserMetricServiceImpl service;

    private static Iterable<UserMetric> userMetrics;

    @BeforeClass
    public static void init(){

        UserMetric userMetric1 = new UserMetric()
            .setViewId("viewId1")
            .setName("requestsNumber")
            .setValue(12d)
            .setId("AWSRequestNumber")
            .setTimestamp(TODAY);
        UserMetric userMetric2 = new UserMetric()
            .setViewId("viewId1")
            .setName("requestsNumber")
            .setValue(12d)
            .setId("AWSRequestNumber")
            .setTimestamp(YESTERDAY);
        UserMetric userMetric3 = new UserMetric()
            .setViewId("viewId1")
            .setName("requestsNumber")
            .setValue(12d)
            .setId("AWSRequestNumber")
            .setTimestamp(THREE_HOURS_AGO);

        UserMetric userMetric4 = new UserMetric().setViewId("viewId1").setName("responseTime").setId("AWSResponseTime").setValue(15d).setSampleSize(100L).setTimestamp(TODAY);
        UserMetric userMetric5 = new UserMetric().setViewId("viewId1").setName("responseTime").setId("AWSResponseTime").setValue(10d).setSampleSize(150L).setTimestamp(TODAY);

        UserMetric userMetric6 = new UserMetric().setViewId("viewId1").setName("availabilityRate").setId("AWSAvailabilityRate").setValue(100d).setTimestamp(TODAY);
        UserMetric userMetric7 = new UserMetric().setViewId("viewId1").setName("availabilityRate").setId("AWSAvailabilityRate").setValue(75d).setTimestamp(TODAY);

        userMetrics = Arrays.asList(userMetric1, userMetric2, userMetric3, userMetric4, userMetric5, userMetric6, userMetric7);
    }

    @Test
    public void testAddingToExistingHistoricUserMetric(){

        service.addToCurrentPeriod(userMetrics);
        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifierAndHistoricType(LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS), "AWSRequestNumber", ChronoUnit.DAYS);

        assertEquals("AWSRequestNumber", result.getIdentifier());
        assertEquals((long) result.getTimestamp(), LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS));
        assertEquals(48, result.getValue(), 0.0);
    }

    @Test
    public void testAddingToUnExistingHistoricUserMetric(){

        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifierAndHistoricType(LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS), "AWSRequestNumber", ChronoUnit.DAYS);

        assertEquals("AWSRequestNumber", result.getIdentifier());
        assertEquals((long) result.getTimestamp(), LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS));
        assertEquals(24, result.getValue(), 0.0);
    }

    @Test
    public void testAddWithSampleSize(){
        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifierAndHistoricType(LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS), "AWSResponseTime", ChronoUnit.DAYS);

        assertEquals("AWSResponseTime", result.getIdentifier());
        assertEquals((long) result.getTimestamp(), LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS));
        assertEquals(250d, result.getSampleSize(), 0.0);
        assertEquals(3000d, result.getValue(), 0.0);
    }

    @Test
    public void testAvailabilityRate(){
        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifierAndHistoricType(LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.HOURS), "AWSAvailabilityRate", ChronoUnit.HOURS);
        assertEquals("AWSAvailabilityRate", result.getIdentifier());
        assertEquals((long) result.getTimestamp(), LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.HOURS));
        assertEquals(2, (long) result.getSampleSize());
        assertEquals(175, result.getValue(), 0.0);
    }

    @After
    public void cleanCollection(){
        repository.deleteAll();
    }

}