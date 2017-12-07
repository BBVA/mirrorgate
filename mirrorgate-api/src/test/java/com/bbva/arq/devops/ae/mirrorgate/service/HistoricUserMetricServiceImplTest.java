package com.bbva.arq.devops.ae.mirrorgate.service;

import static org.junit.Assert.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
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
            .setName("requestNumber")
            .setSampleSize(12d)
            .setId("AWSRequestNumber")
            .setTimestamp(1512389543l);
        UserMetric userMetric2 = new UserMetric()
            .setName("requestNumber")
            .setSampleSize(12d)
            .setId("AWSRequestNumber")
            .setTimestamp(1492002720l);
        UserMetric userMetric3 = new UserMetric()
            .setName("requestNumber")
            .setSampleSize(12d)
            .setId("AWSRequestNumber")
            .setTimestamp(1491996840l);
        UserMetric userMetric4 = new UserMetric().setName("notRequestNumber").setSampleSize(12d).setId("AWSRequestNumber");

        userMetrics = Arrays.asList(userMetric1, userMetric2, userMetric3, userMetric4);
    }

    @Test
    public void testAddingToExistingHistoricUserMetric(){

        service.addToCurrentPeriod(userMetrics);
        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifier(1512388800l, "AWSRequestNumber");

        assertTrue(result.getIdentifier().equals("AWSRequestNumber"));
        assertTrue(result.getTimestamp() == 1512388800l);
        assertTrue(result.getSampleSize() == 24);
    }

    @Test
    public void testAddingToUnExistingHistoricUserMetric(){

        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifier(1512388800l, "AWSRequestNumber");

        assertTrue(result.getIdentifier().equals("AWSRequestNumber"));
        assertTrue(result.getTimestamp() == 1512388800l);
        assertTrue(result.getSampleSize() == 12);
    }


    @Test
    public void testRemoveExtraPeriods(){

        service.addToCurrentPeriod(userMetrics);

        assertTrue(repository.count() == 3);

        service.removeExtraPeriodsForMetricAndIdentifier(2, "requestNumber","AWSRequestNumber");

        assertTrue(repository.count() == 2);
    }

    @Test
    public void testRemoveExtraPeriodsNoPeriodsAvailable(){

        assertTrue(repository.count() == 0);

        service.removeExtraPeriodsForMetricAndIdentifier(2, "requestNumber","AWSRequestNumber");

        assertTrue(repository.count() == 0);
    }

    @Test
    public void testRemoveExtraPeriodsNoNeedToDelete(){

        service.addToCurrentPeriod(userMetrics);

        assertTrue(repository.count() == 3);

        service.removeExtraPeriodsForMetricAndIdentifier(3, "requestNumber","AWSRequestNumber");

        assertTrue(repository.count() == 3);
    }


    @After
    public void cleanCollection(){
        repository.deleteAll();
    }

}