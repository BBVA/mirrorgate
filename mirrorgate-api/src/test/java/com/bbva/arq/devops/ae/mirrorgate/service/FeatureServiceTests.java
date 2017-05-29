/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bbva.arq.devops.ae.mirrorgate.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FeatureServiceTests {

    @Mock private FeatureRepository featureRepository;
    @InjectMocks private FeatureServiceImpl featureService;

    @Test
    public void getActiveUserStoriesByProjectNameTest() {
        String dashboardName = "mirrorgate";

        Feature story1 = new Feature();
        story1.setId(ObjectId.get());
        story1.setsSprintAssetState("Active");
        story1.setsProjectName(dashboardName);

        Feature story2= new Feature();
        story2.setId(ObjectId.get());
        story2.setsSprintAssetState("Active");
        story2.setsProjectName(dashboardName);

        List<Feature> stories = new ArrayList<>();
        stories.add(story1);
        stories.add(story2);

        when(featureRepository.findActiveUserStoriesByBoards(Arrays.asList(dashboardName),
                new Sort(new Order("sStatus")))).thenReturn(stories);

        List<Feature> activeStoriesByDashboardName
                = featureService.getActiveUserStoriesByBoards(Arrays.asList(dashboardName));
        verify(featureRepository, times(1))
                .findActiveUserStoriesByBoards(Arrays.asList(dashboardName), new Sort(new Order("sStatus")));

        assertThat(activeStoriesByDashboardName.get(0)).isEqualTo(story1);
        assertThat(activeStoriesByDashboardName.get(1)).isEqualTo(story2);
    }

}
