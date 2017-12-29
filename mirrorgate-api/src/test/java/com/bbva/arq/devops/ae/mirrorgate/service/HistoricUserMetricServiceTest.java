package com.bbva.arq.devops.ae.mirrorgate.service;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricTendenciesDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class HistoricUserMetricServiceTest {

    @Mock
    private HistoricUserMetricRepository historicUserMetricRepository;

    @InjectMocks
    private HistoricUserMetricServiceImpl historicUserMetricService;


    @Test
    public void testAveragesWithHistoric(){

        HistoricUserMetric historicUserMetricLong = new HistoricUserMetric();
        historicUserMetricLong.setValue(8d);
        HistoricUserMetric historicUserMetricShort = new HistoricUserMetric();
        historicUserMetricShort.setValue(12d);

        List<HistoricUserMetric> listOfHistoricMetrics = new ArrayList<>();

        for(int i=0; i<96; i++){
            listOfHistoricMetrics.add(historicUserMetricShort);
        }

        for(int i=0; i<96; i++){
            listOfHistoricMetrics.add(historicUserMetricLong);
        }

        when(historicUserMetricRepository.findAllByViewIdInAndValueGreaterThanAndNameAndHistoricTypeOrderByTimestampAsc(any(
            PageRequest.class),anyListOf(String.class), anyDouble(), anyString(), any(ChronoUnit.class)))
            .thenReturn(listOfHistoricMetrics);

        HistoricTendenciesDTO historicTendenciesDTO =
            historicUserMetricService.getHistoricMetricsForDashboard(new DashboardDTO().setAnalyticViews(
                Arrays.asList("view")), "requestNumber");

        assertTrue(historicTendenciesDTO.getShortTermTendency() == 0);
        assertTrue(historicTendenciesDTO.getLongTermTendency() == 20d);
    }

    @Test
    public void testAveragesWithNoHistoric(){

        when(historicUserMetricRepository.findAllByViewIdInAndValueGreaterThanAndNameAndHistoricTypeOrderByTimestampAsc(any(
            PageRequest.class),anyListOf(String.class), anyDouble(), anyString(), any(ChronoUnit.class)))
            .thenReturn(new ArrayList<>());

        HistoricTendenciesDTO historicTendenciesDTO =
            historicUserMetricService.getHistoricMetricsForDashboard(new DashboardDTO().setAnalyticViews(
                Arrays.asList("view")), "requestNumber");

        assertTrue(historicTendenciesDTO.getShortTermTendency() == 0);
        assertTrue(historicTendenciesDTO.getLongTermTendency() == 0);
    }

}
