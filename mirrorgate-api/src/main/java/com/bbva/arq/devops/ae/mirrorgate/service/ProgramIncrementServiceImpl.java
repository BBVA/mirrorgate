package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl.ProgramIncrementNamesAggregationResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgramIncrementServiceImpl implements ProgramIncrementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgramIncrementServiceImpl.class);

    private FeatureRepository featureRepository;
    private DashboardRepository dashboardRepository;

    @Autowired
    public ProgramIncrementServiceImpl(FeatureRepository featureRepository,
                                   DashboardRepository dashboardRepository){

        this.dashboardRepository = dashboardRepository;
        this.featureRepository = featureRepository;
    }

    public List<Feature> getProgramIncrementFeatures(String dashboardName){

        List<Feature> piFeatures = null;

        LOGGER.debug("Getting product increment information for dashboard : {}", dashboardName);

        Dashboard dashboard = dashboardRepository.findOneByName(dashboardName);

        if(dashboard != null) {
            List<String> boards = dashboard.getBoards();
            Optional<String> productIncrementExpression = Optional
                .ofNullable(dashboard.getProgramIncrement());

            String currentPIName = getProductIncrementNameForBoard(boards,
                productIncrementExpression);

            LOGGER.debug("Dashboard current product increment : {}", currentPIName);

            piFeatures = featureRepository.findProductIncrementFeatures(currentPIName);
        }

        return piFeatures;
    }

    String getProductIncrementNameForBoard(List<String> boards, Optional<String> productIncrementExpression){

        ProgramIncrementNamesAggregationResult result = featureRepository.getProductIncrementFromFeatures(boards);

        List<String> piNames = null;
        if (result != null) {
            piNames = result.getPiNames();
        }

        if(productIncrementExpression.isPresent() && piNames != null) {

            Pattern piRegex = Pattern.compile("^" + productIncrementExpression.get() + "$");

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

    private boolean findIfLocalDateIsInRange(String date1, String date2){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dateTime1 = LocalDate.parse(date1, formatter);
        LocalDate dateTime2 = LocalDate.parse(date2, formatter);

        LocalDate now = LocalDate.now();

        return (now.isEqual(dateTime1) || now.isEqual(dateTime2)) ||
               (dateTime1.isBefore(now) && dateTime2.isAfter(now));
    }

}
