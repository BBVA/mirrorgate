package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepositoryImpl.PINamesAggregationResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductIncrementService {

    private Pattern dateRegex = Pattern.compile("(?<date1>[0-9]{4}/[0-9]{2}/[0-9]{2})-(?<date2>[0-9]{4}/[0-9]{2}/[0-9]{2})");

    private FeatureRepository featureRepository;
    private DashboardRepository dashboardRepository;

    @Autowired
    public ProductIncrementService(FeatureRepository featureRepository,
                                   DashboardRepository dashboardRepository){

        this.dashboardRepository = dashboardRepository;
        this.featureRepository = featureRepository;
    }

    public List<Feature> getProductIncrementFeatures(String dashboardName){

        Dashboard dashboard = dashboardRepository.findOneByName(dashboardName);
        List<String> boards = dashboard.getBoards();

        String currentPIName = getProductIncrementNameForBoard(boards);

        return featureRepository.findProductIncrementFeatures(currentPIName);
    }

    public String getProductIncrementNameForBoard(List<String> boards){

        PINamesAggregationResult result = featureRepository.getProductIncrementFromFeatures(boards);

        List<String> piNames = null;
        if (result != null) {
            piNames = result.getPiNames();
        }

        for(int i=0; i<piNames.size(); i++){

            String piName = piNames.get(i);
            Matcher matcher = dateRegex.matcher(piName);

            while(matcher.find()){
                //get groups
                String date1 = matcher.group("date1");
                String date2 = matcher.group("date2");

                if(findIfLocalDateIsInRange(date1, date2)){
                    //This is our PI!
                    return piName;
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
