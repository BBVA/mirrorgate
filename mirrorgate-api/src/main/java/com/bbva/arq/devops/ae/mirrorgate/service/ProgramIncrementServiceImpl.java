package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.ProgramIncrementDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.IssueMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl.ProgramIncrementNamesAggregationResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProgramIncrementServiceImpl implements ProgramIncrementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgramIncrementServiceImpl.class);
    private static final Sort SORT_BY_LAST_MODIFICATION = new Sort(Sort.Direction.DESC, "lastModification");

    private FeatureService featureService;
    private DashboardService dashboardService;

    @Autowired
    public ProgramIncrementServiceImpl(FeatureService featureService,
                                   DashboardService dashboardService){

        this.dashboardService = dashboardService;
        this.featureService = featureService;
    }

    @Override
    public ProgramIncrementDTO getProgramIncrementFeatures(String dashboardName){
        LOGGER.debug("Getting product increment information for dashboard : {}", dashboardName);

        Dashboard dashboard = dashboardService.getDashboard(dashboardName);

        String currentPIName = getCurrentProgramIncrementName(dashboard);

        if(currentPIName == null) {
            return new ProgramIncrementDTO(null, null);
        }

        List<Feature> piFeatures = featureService.getProductIncrementFeatures(currentPIName);

        List<String> piFeaturesKeys = piFeatures
                                        .stream()
                                        .map(Feature::getsNumber)
                                        .collect(Collectors.toList());

        List<String> boardPIFeaturesKeys = featureService.getProgramIncrementFeaturesByBoard(dashboard.getBoards(), piFeaturesKeys);

        return createResponse(dashboard, piFeatures, boardPIFeaturesKeys);

    }

    private String getCurrentProgramIncrementName(Dashboard dashboard){
        return getProductIncrementNameForExpression(dashboard.getProgramIncrement());
    }

    String getProductIncrementNameForExpression(String productIncrementExpression){

        Pattern piRegex = Pattern.compile("^" + productIncrementExpression + "$");

        ProgramIncrementNamesAggregationResult result = featureService.getProductIncrementFromPiPattern(piRegex);

        List<String> piNames = null;
        if (result != null) {
            piNames = result.getPiNames();
        }

        if(piNames != null) {


            for (int i = 0; i < piNames.size(); i++) {

                String piName = piNames.get(i);
                Matcher matcher = piRegex.matcher(piName);

                while (matcher.find()) {
                    if (matcher.groupCount() > 1) {
                        //get groups
                        String startDate = matcher.group("startDate");
                        String endDate = matcher.group("endDate");

                        if (findIfLocalDateIsInRange(startDate, endDate)) {
                            return piName;
                        }
                    } else {
                        return matcher.group();
                    }
                }
            }
        }
        return null;
    }

    private ProgramIncrementDTO createResponse(Dashboard dashboard, List<Feature> piFeatures, List<String> boardPIFeaturesKeys){

        //Get features belonging to this board
        List<IssueDTO> boardPIFeatures = piFeatures.stream()
            .filter(f -> boardPIFeaturesKeys.contains(f.getsNumber()) || containsDashboardKeyword(dashboard, f))
            .map(IssueMapper::map)
            .collect(Collectors.toList());

        List<String> keys = boardPIFeatures.stream()
                .map(IssueDTO::getJiraKey)
                .collect(Collectors.toList());

        List<Feature> piIssues = featureService.getFeatureRelatedIssues(keys);

        List<IssueDTO> boardPIIssues = piIssues.stream()
                .filter((f) -> containsDashboardKeyword(dashboard, f))
                .map(IssueMapper::map)
                .collect(Collectors.toList());

        return new ProgramIncrementDTO(boardPIFeatures, boardPIIssues);
    }

    private boolean containsDashboardKeyword(Dashboard dashboard, Feature f) {
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
