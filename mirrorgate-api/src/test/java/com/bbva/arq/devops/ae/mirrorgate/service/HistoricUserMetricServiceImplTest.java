package com.bbva.arq.devops.ae.mirrorgate.service;

import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.*;
import static org.junit.Assert.assertTrue;

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
            .setName("requestsNumber")
            .setValue(12d)
            .setId("AWSRequestNumber")
                .setTimestamp(TODAY);
        UserMetric userMetric2 = new UserMetric()
            .setName("requestsNumber")
            .setValue(12d)
            .setId("AWSRequestNumber")
                .setTimestamp(YESTERDAY);
        UserMetric userMetric3 = new UserMetric()
            .setName("requestsNumber")
            .setValue(12d)
            .setId("AWSRequestNumber")
                .setTimestamp(ONE_MONTH_AGO);

        UserMetric userMetric4 = new UserMetric().setName("responseTime").setId("AWSResponseTime").setValue(15d).setSampleSize(100d).setTimestamp(TODAY);;
        UserMetric userMetric5 = new UserMetric().setName("responseTime").setId("AWSResponseTime").setValue(10d).setSampleSize(150d).setTimestamp(TODAY);;

        userMetrics = Arrays.asList(userMetric1, userMetric2, userMetric3, userMetric4, userMetric5);
    }

    @Test
    public void testAddingToExistingHistoricUserMetric(){

        service.addToCurrentPeriod(userMetrics);
        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifier(LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.HOURS), "AWSRequestNumber");

        assertTrue(result.getIdentifier().equals("AWSRequestNumber"));
        assertTrue(result.getTimestamp() == LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.HOURS));
        assertTrue(result.getValue() == 24);
    }

    @Test
    public void testAddingToUnExistingHistoricUserMetric(){

        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifier(LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.HOURS), "AWSRequestNumber");

        assertTrue(result.getIdentifier().equals("AWSRequestNumber"));
        assertTrue(result.getTimestamp() == LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.HOURS));
        assertTrue(result.getValue() == 12);
    }


    @Test
    public void testRemoveExtraPeriods(){

        service.addToCurrentPeriod(userMetrics);

        assertTrue(repository.count() == 4);

        service.removeExtraPeriodsForMetricAndIdentifier(2, "requestsNumber","AWSRequestNumber");

        assertTrue(repository.count() == 3);
    }

    @Test
    public void testRemoveExtraPeriodsNoPeriodsAvailable(){

        assertTrue(repository.count() == 0);

        service.removeExtraPeriodsForMetricAndIdentifier(2, "requestsNumber","AWSRequestNumber");

        assertTrue(repository.count() == 0);
    }

    @Test
    public void testRemoveExtraPeriodsNoNeedToDelete(){

        service.addToCurrentPeriod(userMetrics);

        assertTrue(repository.count() == 4);

        service.removeExtraPeriodsForMetricAndIdentifier(30, "requestNumber","AWSRequestNumber");

        assertTrue(repository.count() == 4);
    }

    @Test
    public void testAddWithSampleSize(){
        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifier(LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.HOURS), "AWSResponseTime");

        assertTrue(result.getIdentifier().equals("AWSResponseTime"));
        assertTrue(result.getTimestamp() == LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.HOURS));
        assertTrue(result.getSampleSize() == 250d);
        assertTrue(result.getValue() == 12d);
    }

    @After
    public void cleanCollection(){
        repository.deleteAll();
    }

}