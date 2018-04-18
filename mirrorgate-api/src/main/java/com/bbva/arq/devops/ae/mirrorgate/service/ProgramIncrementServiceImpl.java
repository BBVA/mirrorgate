package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.ProgramIncrementDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.IssueMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl.ProgramIncrementNamesAggregationResult;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgramIncrementServiceImpl implements ProgramIncrementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgramIncrementServiceImpl.class);

    private final FeatureService featureService;
    private final DashboardService dashboardService;

    @Autowired
    public ProgramIncrementServiceImpl(FeatureService featureService,
                                   DashboardService dashboardService){

        this.dashboardService = dashboardService;
        this.featureService = featureService;
    }

    @Override
    public ProgramIncrementDTO getProgramIncrementFeatures(String dashboardName){
        LOGGER.debug("Getting product increment information for dashboard : {}", dashboardName);

        DashboardDTO dashboard = dashboardService.getDashboard(dashboardName);

        ProgramIncrementDTO piDTO = getCurrentProgramIncrementNameAndDates(dashboard);

        if(piDTO == null){
            return new ProgramIncrementDTO();
        }

        return createResponse(dashboard, piDTO)
                .setProgramIncrementName(piDTO.getProgramIncrementName())
                .setProgramIncrementStartDate(piDTO.getProgramIncrementStartDate())
                .setProgramIncrementEndDate(piDTO.getProgramIncrementEndDate());

    }

    private ProgramIncrementDTO getCurrentProgramIncrementNameAndDates(DashboardDTO dashboard){
        return getProductIncrementNameAndDatesForExpression(dashboard.getProgramIncrement());
    }

    ProgramIncrementDTO getProductIncrementNameAndDatesForExpression(String productIncrementExpression){

        Pattern piRegex = Pattern.compile("^" + productIncrementExpression + "$");

        ProgramIncrementNamesAggregationResult result = featureService.getProductIncrementFromPiPattern(piRegex);

        List<String> piNames = null;
        if (result != null) {
            piNames = result.getPiNames();
        }

        if(piNames != null) {

            piNames = piNames.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

            for (int i = 0; i < piNames.size(); i++) {

                String piName = piNames.get(i);
                Matcher matcher = piRegex.matcher(piName);

                while (matcher.find()) {
                    if (matcher.groupCount() > 1) {
                        //get groups
                        String startDate = matcher.group("startDate");
                        String endDate = matcher.group("endDate");

                        if (findIfLocalDateIsInRange(startDate, endDate)) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

                            Date programIncrementStartDate = null;
                            Date programIncrementEndDate = null;

                            try {
                                programIncrementStartDate = formatter.parse(startDate);
                                programIncrementEndDate = formatter.parse(endDate);

                            } catch (ParseException e) {
                                LOGGER.error("Parse exception", e);
                            }

                            return new ProgramIncrementDTO()
                                .setProgramIncrementName(piName)
                                .setProgramIncrementStartDate(programIncrementStartDate)
                                .setProgramIncrementEndDate(programIncrementEndDate);

                        }
                    } else {
                        return new ProgramIncrementDTO().setProgramIncrementName(matcher.group());
                    }
                }
            }
        }
        return null;
    }

    private ProgramIncrementDTO createResponse(DashboardDTO dashboard, ProgramIncrementDTO piDTO) {

        //Get features belonging to this board and this PI
        List<Feature> piFeatures = featureService.getProductIncrementFeatures(piDTO.getProgramIncrementName());
        List<String> piFeaturesKeys = piFeatures
                .stream()
                .map(Feature::getsNumber)
                .collect(Collectors.toList());
        List<String> boardPIFeaturesKeys = featureService.getProgramIncrementFeaturesByBoard(dashboard.getBoards(), piFeaturesKeys);
        List<IssueDTO> boardPIFeatures = piFeatures.stream()
                .filter(f -> boardPIFeaturesKeys.contains(f.getsNumber()) || containsDashboardKeyword(dashboard, f))
                .map(IssueMapper::map)
                .collect(Collectors.toList());

        //Get stories belonging to this board and this PI
        List<String> keys = boardPIFeatures.stream()
                .map(IssueDTO::getJiraKey)
                .collect(Collectors.toList());
        List<Feature> piIssues = featureService.getFeatureRelatedIssues(keys);
        List<IssueDTO> boardPIIssues = piIssues.stream()
                .filter((f) -> containsDashboardKeyword(dashboard, f))
                .map(IssueMapper::map)
                .collect(Collectors.toList());

        //Get epics belonging to this board and this PI
        List<String> parentKeys;
        parentKeys = piFeatures.stream()
                .filter(f -> f.getsParentKey() != null)
                .map(Feature::getsParentKey)
                .flatMap(l -> l.stream())
                .collect(Collectors.toList());
        List<Feature> piEpics = featureService.getEpicsBySNumber(parentKeys);
        List<IssueDTO> boardPIEpics = piEpics.stream()
                .filter((f) -> containsDashboardKeyword(dashboard, f))
                .map(IssueMapper::map)
                .collect(Collectors.toList());

        return new ProgramIncrementDTO()
                .setProgramIncrementEpics(boardPIEpics)
                .setProgramIncrementFeatures(boardPIFeatures)
                .setProgramIncrementStories(boardPIIssues);
    }

    private boolean containsDashboardKeyword(DashboardDTO dashboard, Feature f) {
        return f.getKeywords() != null && f.getKeywords().stream()
                .filter((k) -> dashboard.getBoards().contains(k))
                .count() > 0;
    }

    private boolean findIfLocalDateIsInRange(String date1, String date2){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dateTime1 = LocalDate.parse(date1, formatter);
        LocalDate dateTime2 = LocalDate.parse(date2, formatter);

        LocalDate now = LocalDate.now();

        return (now.isEqual(dateTime1) || now.isEqual(dateTime2)) ||
               (dateTime1.isBefore(now) && dateTime2.isAfter(now));
    }

}
