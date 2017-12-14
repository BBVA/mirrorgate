package com.bbva.arq.devops.ae.mirrorgate.service;

import static org.junit.Assert.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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

    private static long date1 = LocalDateTime.now(ZoneId.of("UTC")).toInstant(ZoneOffset.UTC).toEpochMilli();
    private static long date2 = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1).toInstant(ZoneOffset.UTC).toEpochMilli();
    private static long date3 = LocalDateTime.now(ZoneId.of("UTC")).minusDays(30).toInstant(ZoneOffset.UTC).toEpochMilli();

    @BeforeClass
    public static void init(){

        UserMetric userMetric1 = new UserMetric()
            .setName("requestsNumber")
            .setValue(12d)
            .setId("AWSRequestNumber")
            .setTimestamp(date1);
        UserMetric userMetric2 = new UserMetric()
            .setName("requestsNumber")
            .setValue(12d)
            .setId("AWSRequestNumber")
            .setTimestamp(date2);
        UserMetric userMetric3 = new UserMetric()
            .setName("requestsNumber")
            .setValue(12d)
            .setId("AWSRequestNumber")
            .setTimestamp(date3);

        UserMetric userMetric4 = new UserMetric().setName("notRequestNumber").setId("AWSRequestNumber").setValue(12d);

        userMetrics = Arrays.asList(userMetric1, userMetric2, userMetric3, userMetric4);
    }

    @Test
    public void testAddingToExistingHistoricUserMetric(){

        service.addToCurrentPeriod(userMetrics);
        service.addToCurrentPeriod(userMetrics);

        repository.findAll();

        HistoricUserMetric result = repository.findByTimestampAndIdentifier(LocalDateTimeHelper.getTimestampPeriod(date1), "AWSRequestNumber");

        assertTrue(result.getIdentifier().equals("AWSRequestNumber"));
        assertTrue(result.getTimestamp() == LocalDateTimeHelper.getTimestampPeriod(date1));
        assertTrue(result.getValue() == 24);
    }

    @Test
    public void testAddingToUnExistingHistoricUserMetric(){

        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifier(LocalDateTimeHelper.getTimestampPeriod(date1), "AWSRequestNumber");

        assertTrue(result.getIdentifier().equals("AWSRequestNumber"));
        assertTrue(result.getTimestamp() == LocalDateTimeHelper.getTimestampPeriod(date1));
        assertTrue(result.getValue() == 12);
    }


    @Test
    public void testRemoveExtraPeriods(){

        service.addToCurrentPeriod(userMetrics);

        assertTrue(repository.count() == 3);

        service.removeExtraPeriodsForMetricAndIdentifier(2, "requestsNumber","AWSRequestNumber");

        assertTrue(repository.count() == 2);
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

        assertTrue(repository.count() == 3);

        service.removeExtraPeriodsForMetricAndIdentifier(30, "requestNumber","AWSRequestNumber");

        assertTrue(repository.count() == 3);
    }


    @After
    public void cleanCollection(){
        repository.deleteAll();
    }

}