/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.service;


import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationReviewsDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private DashboardRepository dashboardRepository;
    private ReviewRepository reviewRepository;

    @Autowired
    public ApplicationServiceImpl(DashboardRepository dashboardRepository, ReviewRepository reviewRepository){
        this.dashboardRepository = dashboardRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ApplicationReviewsDTO> getApplicationsAndReviews(){

        List<String> activeApplicationNames = getApplicationNames(getActiveApplications());

        List<ApplicationReviewsDTO> appsWithReview = reviewRepository.getLastReviewPerApplication(activeApplicationNames);
        List<String> appsWithoutReview = getApplicationsWithoutReviews(activeApplicationNames, appsWithReview);

        List<ApplicationReviewsDTO> appsReviews = new ArrayList<>();

        appsReviews.addAll(buildApplicationsWithoutReviews(appsWithoutReview));
        appsReviews.addAll(appsWithReview);

        return appsReviews;
    }

    private List<String> getApplicationsWithoutReviews(List<String> activeApplicationNames,
        List<ApplicationReviewsDTO> appsWithReview){

        List<String> applicationWithoutReviewNames = new ArrayList<>();
        applicationWithoutReviewNames.addAll(activeApplicationNames);

        List<String> applicationsWithReviewNames =
            appsWithReview
                .stream()
                .map(ApplicationReviewsDTO::getAppName)
                .collect(Collectors.toList());

        applicationWithoutReviewNames.removeAll(applicationsWithReviewNames);

        return applicationWithoutReviewNames;
    }

    private List<ApplicationReviewsDTO> buildApplicationsWithoutReviews(List<String> activeApplicationNames){

        List<ApplicationReviewsDTO> applicationsWithoutReviews = new ArrayList<>();

        activeApplicationNames.forEach(
            name -> {
                ApplicationReviewsDTO newApplicationReview = new ApplicationReviewsDTO();

                newApplicationReview.setAppName(name);
                newApplicationReview.setAppId(name);
                newApplicationReview.setCommentId("0");

                applicationsWithoutReviews.add(newApplicationReview);
            }
        );

        return applicationsWithoutReviews;
    }

    private List<DashboardDTO> getActiveApplications() {
        return dashboardRepository.getActiveDashboards();
    }

    private List<String> getApplicationNames(Iterable<DashboardDTO> activeDashboards) {
        List<String> appNames = new ArrayList<>();
        activeDashboards.forEach(dashboard -> {
            List<String> dApps = dashboard.getApplications();
            if(dApps != null) {
                appNames.addAll(dApps);
            }
        });

        return appNames;
    }

}
