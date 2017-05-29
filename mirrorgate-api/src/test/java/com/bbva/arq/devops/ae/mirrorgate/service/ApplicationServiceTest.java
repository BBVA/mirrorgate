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

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.when;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationReviewsDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ApplicationServiceTest {

    //TODO improve tests
    @Mock
    private DashboardRepository dashboardRepository;
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Test
    public void testApplicationService(){
        Dashboard dashboard1 = createDashboard("mirrorgate", Arrays.asList("mirrorgate", "mood"));
        Dashboard dashboard2 = createDashboard("samuel", Arrays.asList("samuel1", "samuel2"));
        List<Dashboard> listOfDashboards = Arrays.asList(dashboard1, dashboard2);

        ApplicationReviewsDTO applicationReviewsDTO1 = createApplicationDTO("mirrorgate", Platform.IOS);
        List<ApplicationReviewsDTO> applicationReviewsDTOList = Arrays.asList(applicationReviewsDTO1);

        when(dashboardRepository.findByStatusNotOrStatusIsNull(DashboardStatus.DELETED)).thenReturn(listOfDashboards);
        when(reviewRepository.getLastReviewPerApplication(anyList())).thenReturn(applicationReviewsDTOList);

        List<ApplicationReviewsDTO> reviews = applicationService.getApplicationsAndReviews();

        assertTrue(reviews.size() == 4);
    }

    @Test
    public void testNoReviewsForAnyApp(){
        Dashboard dashboard1 = createDashboard("mirrorgate", Arrays.asList("mirrorgate", "mood"));
        Dashboard dashboard2 = createDashboard("samuel", Arrays.asList("samuel1", "samuel2"));
        List<Dashboard> listOfDashboards = Arrays.asList(dashboard1, dashboard2);

        List<ApplicationReviewsDTO> applicationReviewsDTOList = new ArrayList<>();

        when(dashboardRepository.findByStatusNotOrStatusIsNull(DashboardStatus.DELETED)).thenReturn(listOfDashboards);
        when(reviewRepository.getLastReviewPerApplication(anyList())).thenReturn(applicationReviewsDTOList);

        List<ApplicationReviewsDTO> reviews = applicationService.getApplicationsAndReviews();

        assertTrue(reviews.size() == 4);
    }


    private Dashboard createDashboard(String name, List<String> applications) {
        Dashboard dashboard = new Dashboard();

        dashboard.setId(ObjectId.get());
        dashboard.setName(name);
        String urlRepo1 = "http.//repo1.git";
        String urlRepo2 = "http.//repo2.git";
        List<String> codeRepos = new ArrayList<>();
        codeRepos.add(urlRepo1);
        codeRepos.add(urlRepo2);
        dashboard.setCodeRepos(codeRepos);
        dashboard.setApplications(applications);

        return dashboard;
    }

    private ApplicationReviewsDTO createApplicationDTO(String name, Platform platform) {

        ApplicationReviewsDTO applicationReviewsDTO = new ApplicationReviewsDTO();

        applicationReviewsDTO.setCommentId("12");
        applicationReviewsDTO.setAppId(name);
        applicationReviewsDTO.setAppName(name);
        applicationReviewsDTO.setPlatform(platform);

        return applicationReviewsDTO;
    }

}
