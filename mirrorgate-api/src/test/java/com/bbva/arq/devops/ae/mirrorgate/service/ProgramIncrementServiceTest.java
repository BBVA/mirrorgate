package com.bbva.arq.devops.ae.mirrorgate.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.ProgramIncrementDTO;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl.ProgramIncrementNamesAggregationResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class ProgramIncrementServiceTest {

    @Mock
    private FeatureService featureService;
    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private ProgramIncrementServiceImpl piService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgramIncrementServiceTest.class);


    @Test
    public void testNoCurrentPIName(){

        when(dashboardService.getDashboard(anyString())).thenReturn(new DashboardDTO());

        ProgramIncrementDTO programIncrementDTO = piService.getProgramIncrementFeatures("MirrorGate");

        assertNull(programIncrementDTO.getProgramIncrementFeatures());
        verify(dashboardService, times(1)).getDashboard(anyString());
    }


    @Test
    public void testProductIncrementNameWithUserDefinedRegex(){

        List<String> piNamesList = Arrays.asList("AE_2017_PI03_(2016/12/04-2017/01/28)", "AE_2017_PI04_(2017/01/28-2017/04/14)", "AE_2016_PI02_(2016/11/04-2016/12/03)");
        ProgramIncrementNamesAggregationResult piNames = new ProgramIncrementNamesAggregationResult(piNamesList);

        when(featureService.getProductIncrementFromPiPattern(any(Pattern.class))).thenReturn(piNames);
        ProgramIncrementDTO activePI = piService.getProductIncrementNameAndDatesForExpression("AE_2017_PI0._.*");

        assertEquals("AE_2017_PI04_(2017/01/28-2017/04/14)", activePI.getProgramIncrementName());
    }

    @Test
    public void testProductIncrementDatesWithUserDefinedRegex(){

        Date expectedStartDate = new Date();
        Date expectedEndDate = new Date();
        String expectedProductIncrement = generateProductIncrementName(2);

        List<String> piNamesList = Arrays.asList("AE_2016_PI02_(2016/11/04-2016/12/03)", "AE_2017_PI03_(" + expectedProductIncrement + ")");
        ProgramIncrementNamesAggregationResult piNames = new ProgramIncrementNamesAggregationResult(piNamesList);

        when(featureService.getProductIncrementFromPiPattern(any(Pattern.class))).thenReturn(piNames);
        ProgramIncrementDTO activePI = piService.getProductIncrementNameAndDatesForExpression("AE.*\\((?<startDate>[0-9]{4}/[0-9]{2}/[0-9]{2})-(?<endDate>[0-9]{4}/[0-9]{2}/[0-9]{2})\\)");

        String [] expectedDates = expectedProductIncrement.split("-");

        try {
            expectedStartDate = new SimpleDateFormat("yyyy/MM/dd").parse(expectedDates[0]);
            expectedEndDate = new SimpleDateFormat("yyyy/MM/dd").parse(expectedDates[1]);
        } catch (ParseException e) {
            LOGGER.error("Parse exception", e);
        }

        assertEquals(expectedStartDate, activePI.getProgramIncrementStartDate());
        assertEquals(expectedEndDate, activePI.getProgramIncrementEndDate());
    }

    @Test
    public void testProductIncrementEndsToday(){

        String expectedProductIncrement = generateProductIncrementName(-2);

        List<String> piNamesList = Arrays.asList(expectedProductIncrement, "2016/11/04-2016/12/03");
        ProgramIncrementNamesAggregationResult piNames = new ProgramIncrementNamesAggregationResult(piNamesList);

        when(featureService.getProductIncrementFromPiPattern(any(Pattern.class))).thenReturn(piNames);
        ProgramIncrementDTO activePI = piService.getProductIncrementNameAndDatesForExpression("(?<startDate>[0-9]{4}/[0-9]{2}/[0-9]{2})-(?<endDate>[0-9]{4}/[0-9]{2}/[0-9]{2})");

        assertEquals(expectedProductIncrement, activePI.getProgramIncrementName());
    }

    @Test
    public void testProductIncrementStartsToday(){

        String expectedProductIncrement = generateProductIncrementName(2);

        List<String> piNamesList = Arrays.asList(expectedProductIncrement, "2016/11/04-2016/12/03");
        ProgramIncrementNamesAggregationResult piNames = new ProgramIncrementNamesAggregationResult(piNamesList);

        when(featureService.getProductIncrementFromPiPattern(any(Pattern.class))).thenReturn(piNames);
        ProgramIncrementDTO activePI = piService.getProductIncrementNameAndDatesForExpression("(?<startDate>[0-9]{4}/[0-9]{2}/[0-9]{2})-(?<endDate>[0-9]{4}/[0-9]{2}/[0-9]{2})");

        assertEquals(expectedProductIncrement, activePI.getProgramIncrementName());
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
