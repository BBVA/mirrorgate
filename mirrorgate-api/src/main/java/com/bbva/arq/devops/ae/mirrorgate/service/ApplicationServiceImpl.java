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
import com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
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
        //TODO refactor to make logic clearer
        List<String> applicationNames = getApplicationNames(getActiveApplications());

        List<ApplicationReviewsDTO> appsWithReview = reviewRepository.getLastReviewPerApplication(applicationNames);

        List<String> applicationsWithReviewNames =
            appsWithReview
                .stream()
                .map(ApplicationReviewsDTO::getAppName)
                .collect(Collectors.toList());

        applicationNames.removeAll(applicationsWithReviewNames);

        List<ApplicationReviewsDTO> appsReviews = new ArrayList<>();

        applicationNames.forEach(
            name -> {
                ApplicationReviewsDTO newApplicationReview = new ApplicationReviewsDTO();

                newApplicationReview.setAppName(name);
                //TODO Ugly hack before we decide how we distinguish between platforms
                newApplicationReview.setPlatform(Platform.IOS);
                newApplicationReview.setAppId(name);
                newApplicationReview.setCommentId("0");

                appsReviews.add(newApplicationReview);
            }
        );

        appsReviews.addAll(appsWithReview);
        return appsReviews;
    }

    private Iterable<Dashboard> getActiveApplications(){
        return dashboardRepository.findByStatusNotOrStatusIsNull(DashboardStatus.DELETED);
    }

    private List<String> getApplicationNames(Iterable<Dashboard> activeDashboards){
        List<String> appNames = new ArrayList<>();
        activeDashboards.forEach(dashboard -> appNames.addAll(dashboard.getApplications()));

        return appNames;
    }

}
