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

import com.bbva.arq.devops.ae.mirrorgate.dto.ApplicationReviewsDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final DashboardService dashboardRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ApplicationServiceImpl(final DashboardService dashboardRepository, final ReviewRepository reviewRepository) {
        this.dashboardRepository = dashboardRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ApplicationReviewsDTO> getApplicationsAndReviews() {

        final List<String> activeApplicationNames = getApplicationNames(dashboardRepository.getActiveDashboards());

        final List<ApplicationReviewsDTO> appsWithReview = reviewRepository
            .getLastReviewPerApplication(activeApplicationNames);
        final Set<String> appsWithoutReview = getApplicationsWithoutReviews(activeApplicationNames, appsWithReview);

        final List<ApplicationReviewsDTO> appsReviews = new ArrayList<>();

        appsReviews.addAll(buildApplicationsWithoutReviews(appsWithoutReview));
        appsReviews.addAll(appsWithReview);

        return appsReviews;
    }

    private Set<String> getApplicationsWithoutReviews(
        final List<String> activeApplicationNames,
        final List<ApplicationReviewsDTO> appsWithReview
    ) {

        final Set<String> applicationWithoutReviewNames = new HashSet<>(activeApplicationNames);

        final List<String> applicationsWithReviewNames = appsWithReview
            .stream()
            .map(ApplicationReviewsDTO::getAppName)
            .collect(Collectors.toList());

        applicationWithoutReviewNames.removeAll(applicationsWithReviewNames);

        return applicationWithoutReviewNames;
    }

    private List<ApplicationReviewsDTO> buildApplicationsWithoutReviews(final Set<String> activeApplicationNames) {

        final List<ApplicationReviewsDTO> applicationsWithoutReviews = new ArrayList<>();

        activeApplicationNames.forEach(
            name -> {
                final ApplicationReviewsDTO newApplicationReview = new ApplicationReviewsDTO();

                newApplicationReview.setAppName(name);
                newApplicationReview.setAppId(name);
                newApplicationReview.setCommentId("0");

                applicationsWithoutReviews.add(newApplicationReview);
            }
        );

        return applicationsWithoutReviews;
    }

    private List<String> getApplicationNames(final Iterable<DashboardDTO> activeDashboards) {
        final List<String> appNames = new ArrayList<>();
        activeDashboards.forEach(dashboard -> {
            final List<String> dApps = dashboard.getApplications();
            if (dApps != null) {
                appNames.addAll(dApps);
            }
        });

        return appNames;
    }
}
