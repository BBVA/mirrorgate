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
import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import com.bbva.arq.devops.ae.mirrorgate.repository.ReviewRepository;
import com.bbva.arq.devops.ae.mirrorgate.util.TestObjectBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        DashboardDTO dashboard1 = TestObjectBuilder.createDashboardDTO("mirrorgate", Arrays.asList("mirrorgate", "mood"));
        DashboardDTO dashboard2 = TestObjectBuilder.createDashboardDTO("samuel", Arrays.asList("samuel1", "samuel2"));
        List<DashboardDTO> listOfDashboards = Arrays.asList(dashboard1, dashboard2);

        ApplicationReviewsDTO applicationReviewsDTO1 = TestObjectBuilder.createApplicationDTO("mirrorgate", Platform.IOS);
        List<ApplicationReviewsDTO> applicationReviewsDTOList = Arrays.asList(applicationReviewsDTO1);

        when(dashboardRepository.getActiveDashboards()).thenReturn(listOfDashboards);
        when(reviewRepository.getLastReviewPerApplication(anyList())).thenReturn(applicationReviewsDTOList);

        List<ApplicationReviewsDTO> reviews = applicationService.getApplicationsAndReviews();

        assertTrue(reviews.size() == 4);
    }

    @Test
    public void testNoReviewsForAnyApp(){
        DashboardDTO dashboard1 = TestObjectBuilder.createDashboardDTO("mirrorgate", Arrays.asList("mirrorgate", "mood"));
        DashboardDTO dashboard2 = TestObjectBuilder.createDashboardDTO("samuel", Arrays.asList("samuel1", "samuel2"));
        List<DashboardDTO> listOfDashboards = Arrays.asList(dashboard1, dashboard2);

        List<ApplicationReviewsDTO> applicationReviewsDTOList = new ArrayList<>();

        when(dashboardRepository.getActiveDashboards()).thenReturn(listOfDashboards);
        when(reviewRepository.getLastReviewPerApplication(anyList())).thenReturn(applicationReviewsDTOList);

        List<ApplicationReviewsDTO> reviews = applicationService.getApplicationsAndReviews();

        assertTrue(reviews.size() == 4);
    }

}
