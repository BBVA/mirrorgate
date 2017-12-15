package com.bbva.arq.devops.ae.mirrorgate.service;


import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricUserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.UserMetricsRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserMetricsServiceTest {

    @Mock
    private DashboardService dashboardService;

    @Mock
    private UserMetricsRepository userMetricsRepository;

    @Mock
    private HistoricUserMetricService historicUserMetricService;

    @InjectMocks
    private UserMetricsServiceImpl userMetricsService;


    @Test
    public void testGetMetricsForDashboard(){
        DashboardDTO dashboardDTO = TestObjectFactory.createDashboardDTO();
        dashboardDTO.setAnalyticViews(Arrays.asList("view"));

        UserMetric userMetrics = new UserMetric().setId("id").setName("requestsNumber");
        when(userMetricsRepository.findAllByViewIdInWithNon0Values(anyListOf(String.class))).thenReturn(Arrays.asList(userMetrics));

        HistoricUserMetricDTO historicUserMetricLong = new HistoricUserMetricDTO().setValue(20d).setAppVersion("123").setName("requestsNumber");
        HistoricUserMetricDTO historicUserMetricShort = new HistoricUserMetricDTO().setValue(10d).setAppVersion("123").setName("requestsNumber");
        List<HistoricUserMetricDTO> historicUserMetrics = Arrays.asList(historicUserMetricShort,historicUserMetricShort,historicUserMetricShort, historicUserMetricLong,historicUserMetricLong,historicUserMetricLong);
        when(historicUserMetricService.getHistoricMetricsForDashboard(dashboardDTO, "requestsNumber", 24)).thenReturn(historicUserMetrics);

        List<UserMetricDTO> userMetricDTO = userMetricsService.getMetricsForDashboard(dashboardDTO);

        assertTrue(userMetricDTO.get(0).getLongTermTendency() == 15);
        assertTrue(userMetricDTO.get(0).getShortTermTendency() == 10);
    }

    @Test
    public void testNoHistoricMetrics(){

        DashboardDTO dashboardDTO = TestObjectFactory.createDashboardDTO();
        dashboardDTO.setAnalyticViews(Arrays.asList("view"));

        UserMetric userMetrics = new UserMetric().setId("id").setName("requestsNumber");
        when(userMetricsRepository.findAllByViewIdInWithNon0Values(anyListOf(String.class))).thenReturn(Arrays.asList(userMetrics));

        List<HistoricUserMetricDTO> historicUserMetrics = new ArrayList<>();
        when(historicUserMetricService.getHistoricMetricsForDashboard(dashboardDTO, "requestsNumber", 24)).thenReturn(historicUserMetrics);

        List<UserMetricDTO> userMetricDTO = userMetricsService.getMetricsForDashboard(dashboardDTO);

        assertTrue(userMetricDTO.get(0).getLongTermTendency() == 0);
        assertTrue(userMetricDTO.get(0).getShortTermTendency() == 0);
    }

}
