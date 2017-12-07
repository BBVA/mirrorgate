package com.bbva.arq.devops.ae.mirrorgate.service;

import static org.junit.Assert.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
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

    private Iterable<UserMetric> userMetrics;

    @Before
    public void init(){

        UserMetric userMetric1 = new UserMetric()
            .setName("requestNumber")
            .setSampleSize(12d)
            .setId("AWSRequestNumber")
            .setTimestamp(1512389543l);
        UserMetric userMetric2 = new UserMetric().setName("notRequestNumber").setSampleSize(12d).setId("AWSRequestNumber");

        userMetrics = Arrays.asList(userMetric1, userMetric2);
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

    @After
    public void cleanCollection(){
        repository.deleteAll();
    }

}