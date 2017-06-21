package com.bbva.arq.devops.ae.mirrorgate.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl.PINamesAggregationResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductIncrementServiceTest {

    @Mock
    private FeatureRepository featureRepository;

    @InjectMocks
    private ProductIncrementService piService;

    //Corner cases: Cuando el sprint acaba o empieza en el día de hoy. Cuando no existe fecha en el nombre del PI. Cuando no existe ningún PI

    @Test
    public void testProductIncrementNotFound(){

        List<String> piNamesList = Arrays.asList("AE_2017_PI03_(2016/12/04-2017/01/28)", "AE_2016_PI02_(2016/11/04-2016/12/03)");
        PINamesAggregationResult piNames = new FeatureRepositoryImpl.PINamesAggregationResult(piNamesList);

        when(featureRepository.getProductIncrementFromFeatures(any(List.class))).thenReturn(piNames);
        String activePIName = piService.getProductIncrementNameForBoard(Arrays.asList("mirrorgate"));

        assertNull(activePIName);
    }

    @Test
    public void testProductIncrementEndsToday(){

        String expectedProductIncrement = generateProductIncrementName(-2);

        List<String> piNamesList = Arrays.asList(expectedProductIncrement, "AE_2016_PI02_(2016/11/04-2016/12/03)");
        PINamesAggregationResult piNames = new FeatureRepositoryImpl.PINamesAggregationResult(piNamesList);

        when(featureRepository.getProductIncrementFromFeatures(any(List.class))).thenReturn(piNames);
        String activePIName = piService.getProductIncrementNameForBoard(Arrays.asList("mirrorgate"));

        assertEquals(activePIName, expectedProductIncrement);
    }

    @Test
    public void testProductIncrementStartsToday(){

        String expectedProductIncrement = generateProductIncrementName(2);

        List<String> piNamesList = Arrays.asList(expectedProductIncrement, "AE_2016_PI02_(2016/11/04-2016/12/03)");
        PINamesAggregationResult piNames = new FeatureRepositoryImpl.PINamesAggregationResult(piNamesList);

        when(featureRepository.getProductIncrementFromFeatures(any(List.class))).thenReturn(piNames);
        String activePIName = piService.getProductIncrementNameForBoard(Arrays.asList("mirrorgate"));

        assertEquals(activePIName, expectedProductIncrement);
    }

    private String generateProductIncrementName(int months){
        String pattern = "AE_2017_PI03_(%1s-%2s)";
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
