package com.bbva.arq.devops.ae.mirrorgate.repository;

import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.THREE_HOURS_AGO;
import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.TODAY;
import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.YESTERDAY;
import static org.junit.Assert.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepositoryImpl.HistoricUserMetricStats;
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

        Iterable<HistoricUserMetric> userMetrics = Arrays
            .asList(userMetric1, userMetric2, userMetric3, userMetric4, userMetric5);

        repository.save(userMetrics);
    }

    @Test
    public void testAverages(){
        List<HistoricUserMetricStats> result =
            repository.getUserMetricAverageTendencyForPeriod(Arrays.asList("ga:155019618"),
                Arrays.asList("responseTime", "requestsNumber", "nonExistent"),
                LocalDateTimeHelper.getTimestampForNUnitsAgo(720, ChronoUnit.HOURS));

        assertTrue(result.size() == 2);
    }

}
