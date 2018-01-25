//TODO add license
package com.bbva.arq.devops.ae.mirrorgate.repository;

import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.THREE_HOURS_AGO;
import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.TODAY;
import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.YESTERDAY;
import static org.junit.Assert.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepositoryImpl.HistoricUserMetricWeightedAverage;
import com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
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
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.HOURS)
            .setName("requestsNumber")
            .setValue(12d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric2 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.HOURS)
            .setName("requestsNumber")
            .setValue(16d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(YESTERDAY);
        HistoricUserMetric userMetric3 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.HOURS)
            .setName("requestsNumber")
            .setValue(12d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(THREE_HOURS_AGO);
        HistoricUserMetric userMetric4 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.HOURS)
            .setName("responseTime")
            .setIdentifier("AWSResponseTime")
            .setValue(15d)
            .setSampleSize(100d)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric5 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.HOURS)
            .setName("responseTime")
            .setIdentifier("AWSResponseTime")
            .setValue(10d)
            .setSampleSize(150d)
            .setTimestamp(TODAY);

        Iterable<HistoricUserMetric> hourlyUserMetrics = Arrays
            .asList(userMetric1, userMetric2, userMetric3, userMetric4, userMetric5);

        repository.save(hourlyUserMetrics);

        HistoricUserMetric userMetric6 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(12d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric7 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(16d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric8 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("requestsNumber")
            .setValue(12d)
            .setIdentifier("AWSRequestNumber")
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric9 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("responseTime")
            .setIdentifier("AWSResponseTime")
            .setValue(15d)
            .setSampleSize(100d)
            .setTimestamp(TODAY);
        HistoricUserMetric userMetric10 = new HistoricUserMetric()
            .setViewId("ga:155019618")
            .setHistoricType(ChronoUnit.MINUTES)
            .setName("responseTime")
            .setIdentifier("AWSResponseTime")
            .setValue(10d)
            .setSampleSize(150d)
            .setTimestamp(TODAY);

        Iterable<HistoricUserMetric> minuteUserMetrics = Arrays
            .asList(userMetric6, userMetric7, userMetric8, userMetric9, userMetric10);

        repository.save(minuteUserMetrics);
    }

    @Test
    public void testOperationMetricsAverages(){
        List<HistoricUserMetricWeightedAverage> result =
            repository.getUserMetricAverageTendencyForPeriod(Arrays.asList("ga:155019618"),
                ChronoUnit.MINUTES,
                Arrays.asList("responseTime", "requestsNumber"),
                LocalDateTimeHelper.getTimestampForNUnitsAgo(10, ChronoUnit.MINUTES));

        assertTrue(result.size() == 2);
    }

}
