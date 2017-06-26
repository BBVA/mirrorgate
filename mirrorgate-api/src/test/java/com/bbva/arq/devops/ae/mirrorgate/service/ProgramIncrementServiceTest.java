package com.bbva.arq.devops.ae.mirrorgate.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl.ProgramIncrementNamesAggregationResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProgramIncrementServiceTest {

    @Mock
    private FeatureRepository featureRepository;
    @Mock
    private DashboardRepository dashboardRepository;

    @InjectMocks
    private ProgramIncrementServiceImpl piService;

    @Test
    public void testNoDashboardFound(){

        List<Feature> featuresList = piService.getProgramIncrementFeatures("MirrorGate");

        assertNull(featuresList);
        verify(dashboardRepository, times(1)).findOneByName(anyString());
        verify(featureRepository, times(0)).getProductIncrementFromFeatures(anyListOf(String.class));
    }

    @Test
    public void testNoCurrentPIName(){

        when(dashboardRepository.findOneByName(anyString())).thenReturn(new Dashboard());

        List<Feature> featuresList = piService.getProgramIncrementFeatures("MirrorGate");

        assertTrue(featuresList.isEmpty());
        verify(dashboardRepository, times(1)).findOneByName(anyString());
        verify(featureRepository, times(1)).getProductIncrementFromFeatures(anyListOf(String.class));
    }


    @Test
    public void testProductIncrementNotFound(){

        List<String> piNamesList = Arrays.asList("AE_2017_PI03_(2016/12/04-2017/01/28)", "AE_2016_PI02_(2016/11/04-2016/12/03)");
        ProgramIncrementNamesAggregationResult piNames = new ProgramIncrementNamesAggregationResult(piNamesList);

        when(featureRepository.getProductIncrementFromFeatures(any(List.class))).thenReturn(piNames);
        String activePIName = piService.getProductIncrementNameForBoard(Arrays.asList("mirrorgate"), Optional.ofNullable(null));

        assertNull(activePIName);
    }

    @Test
    public void testProductIncrementWithUserDefinedRegex(){

        List<String> piNamesList = Arrays.asList("AE_2017_PI03_(2016/12/04-2017/01/28)", "AE_2016_PI02_(2016/11/04-2016/12/03)");
        ProgramIncrementNamesAggregationResult piNames = new ProgramIncrementNamesAggregationResult(piNamesList);

        when(featureRepository.getProductIncrementFromFeatures(any(List.class))).thenReturn(piNames);
        String activePIName = piService.getProductIncrementNameForBoard(Arrays.asList("mirrorgate"),
            Optional.of("AE_2017_PI03_.*"));

        assertEquals(activePIName, "AE_2017_PI03_(2016/12/04-2017/01/28)");
    }

    @Test
    public void testProductIncrementEndsToday(){

        String expectedProductIncrement = generateProductIncrementName(-2);

        List<String> piNamesList = Arrays.asList(expectedProductIncrement, "2016/11/04-2016/12/03");
        ProgramIncrementNamesAggregationResult piNames = new ProgramIncrementNamesAggregationResult(piNamesList);

        when(featureRepository.getProductIncrementFromFeatures(any(List.class))).thenReturn(piNames);
        String activePIName = piService.getProductIncrementNameForBoard(Arrays.asList("mirrorgate"),
            Optional.of("(?<startDate>[0-9]{4}/[0-9]{2}/[0-9]{2})-(?<endDate>[0-9]{4}/[0-9]{2}/[0-9]{2})"));

        assertEquals(activePIName, expectedProductIncrement);
    }

    @Test
    public void testProductIncrementStartsToday(){

        String expectedProductIncrement = generateProductIncrementName(2);

        List<String> piNamesList = Arrays.asList(expectedProductIncrement, "2016/11/04-2016/12/03");
        ProgramIncrementNamesAggregationResult piNames = new ProgramIncrementNamesAggregationResult(piNamesList);

        when(featureRepository.getProductIncrementFromFeatures(any(List.class))).thenReturn(piNames);
        String activePIName = piService.getProductIncrementNameForBoard(Arrays.asList("mirrorgate"),
            Optional.of("(?<startDate>[0-9]{4}/[0-9]{2}/[0-9]{2})-(?<endDate>[0-9]{4}/[0-9]{2}/[0-9]{2})"));

        assertEquals(activePIName, expectedProductIncrement);
    }

    private String generateProductIncrementName(int months){
        String pattern = "%1s-%2s";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        LocalDate now = LocalDate.now();
        LocalDate changeMonthDate = now.plusMonths(months);
        String formattedChangeMonthDate = changeMonthDate.format(formatter);
        String formattedNow = now.format(formatter);

        return months < 0 ?
            String.format(pattern,formattedChangeMonthDate,formattedNow) :
            String.format(pattern,formattedNow ,formattedChangeMonthDate);
    }
}
