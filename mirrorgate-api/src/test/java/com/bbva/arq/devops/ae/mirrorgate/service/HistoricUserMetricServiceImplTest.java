package com.bbva.arq.devops.ae.mirrorgate.service;

import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.THREE_HOURS_AGO;
import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.TODAY;
import static com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeUtils.YESTERDAY;
import static org.junit.Assert.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricTendenciesDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
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

        UserMetric userMetric4 = new UserMetric().setViewId("viewId1").setName("responseTime").setId("AWSResponseTime").setValue(15d).setSampleSize(100d).setTimestamp(TODAY);
        UserMetric userMetric5 = new UserMetric().setViewId("viewId1").setName("responseTime").setId("AWSResponseTime").setValue(10d).setSampleSize(150d).setTimestamp(TODAY);

        userMetrics = Arrays.asList(userMetric1, userMetric2, userMetric3, userMetric4, userMetric5);
    }

    @Test
    public void testAddingToExistingHistoricUserMetric(){

        service.addToCurrentPeriod(userMetrics);
        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifierAndHistoricType(LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS), "AWSRequestNumber", ChronoUnit.DAYS);

        assertTrue(result.getIdentifier().equals("AWSRequestNumber"));
        assertTrue(result.getTimestamp() == LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS));
        assertTrue(result.getValue() == 48);
    }

    @Test
    public void testAddingToUnExistingHistoricUserMetric(){

        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifierAndHistoricType(LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS), "AWSRequestNumber", ChronoUnit.DAYS);

        assertTrue(result.getIdentifier().equals("AWSRequestNumber"));
        assertTrue(result.getTimestamp() == LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS));
        assertTrue(result.getValue() == 24);
    }

    @Test
    public void testRemoveExtraPeriodsNoPeriodsAvailable(){

        assertTrue(repository.count() == 0);

        service.removeExtraPeriodsForMetricAndIdentifier("requestsNumber","AWSRequestNumber", ChronoUnit.DAYS, LocalDateTimeHelper.getTimestampForNUnitsAgo(2, ChronoUnit.DAYS));

        assertTrue(repository.count() == 0);
    }

    @Test
    public void testRemoveExtraPeriodsNoNeedToDelete(){

        service.addToCurrentPeriod(userMetrics);

        assertTrue(repository.count() == 11);

        service.removeExtraPeriodsForMetricAndIdentifier( "requestNumber","AWSRequestNumber", ChronoUnit.DAYS, LocalDateTimeHelper.getTimestampForNUnitsAgo(30, ChronoUnit.DAYS));

        assertTrue(repository.count() == 11);
    }

    @Test
    public void testAddWithSampleSize(){
        service.addToCurrentPeriod(userMetrics);

        HistoricUserMetric result = repository.findByTimestampAndIdentifierAndHistoricType(LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS), "AWSResponseTime", ChronoUnit.DAYS);

        assertTrue(result.getIdentifier().equals("AWSResponseTime"));
        assertTrue(result.getTimestamp() == LocalDateTimeHelper.getTimestampPeriod(TODAY, ChronoUnit.DAYS));
        assertTrue(result.getSampleSize() == 250d);
        assertTrue(result.getValue() == 12d);
    }

    @Test
    public void testGetHistoricMetricsForDashboardZeroValues(){
        HistoricUserMetric historicUserMetric = new HistoricUserMetric()
            .setAppVersion("version")
            .setValue(0d)
            .setSampleSize(0d)
            .setHistoricType(ChronoUnit.DAYS)
            .setName("errorsNumber")
            .setViewId("AWS/850951215438")
            .setTimestamp(TODAY);

        DashboardDTO dashboardDTO = new DashboardDTO().setAnalyticViews(Arrays.asList("AWS/850951215438"));

        repository.save(historicUserMetric);

        Map<String, HistoricTendenciesDTO> map = service.getHistoricMetricsForDashboard(dashboardDTO, Arrays.asList("errorsNumber"));

        assertTrue(map.get("errorsNumber").getLongTermTendency() == 0);
    }


    @After
    public void cleanCollection(){
        repository.deleteAll();
    }

}