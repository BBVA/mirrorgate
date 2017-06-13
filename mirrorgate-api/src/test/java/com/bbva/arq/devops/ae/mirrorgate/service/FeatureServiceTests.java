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

import com.bbva.arq.devops.ae.mirrorgate.core.utils.IssueStatus;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.IssueType;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepository;
import com.bbva.arq.devops.ae.mirrorgate.util.TestObjectBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        Dashboard dashboard = TestObjectBuilder.createDashboard();

        Feature story1 = TestObjectBuilder.createActiveStory();
        Feature story2 = TestObjectBuilder.createActiveStory();

        List<Feature> stories = new ArrayList<>();
        stories.add(story1);
        stories.add(story2);

        when(featureRepository.findActiveUserStoriesByBoards(Arrays.asList(dashboard.getName()),
                new Sort(new Order("sStatus")))).thenReturn(stories);

        List<Feature> activeStoriesByDashboardName
                = featureService.getActiveUserStoriesByBoards(Arrays.asList(dashboard.getName()));
        verify(featureRepository, times(1))
                .findActiveUserStoriesByBoards(Arrays.asList(dashboard.getName()), new Sort(new Order("sStatus")));

        assertThat(activeStoriesByDashboardName.get(0)).isEqualTo(story1);
        assertThat(activeStoriesByDashboardName.get(1)).isEqualTo(story2);
    }

    @Test
    public void getActiveIncidencesByBoardsTest() {

        Dashboard dashboard = TestObjectBuilder.createDashboard();

        Feature inc1 = TestObjectBuilder.createIncidence();
        Feature inc2 = TestObjectBuilder.createIncidence();

        List<Feature> incidences = new ArrayList<>();
        incidences.add(inc1);
        incidences.add(inc2);

        when(featureRepository.findBySProjectNameInAndSTypeNameAndSStatusNot(
                Arrays.asList(dashboard.getName()),
                IssueType.BUG.getName(),
                IssueStatus.DONE.getName())
        ).thenReturn(incidences);

        Iterable<Feature> activeIncidencesByDashboardName
                = featureService.getActiveIncidencesByBoards(Arrays.asList(dashboard.getName()));
        verify(featureRepository, times(1))
                .findBySProjectNameInAndSTypeNameAndSStatusNot(
                        Arrays.asList(dashboard.getName()),
                        IssueType.BUG.getName(),
                        IssueStatus.DONE.getName()
                );

        assertThat(activeIncidencesByDashboardName.iterator().next().getsTypeName()).isEqualTo(IssueType.BUG.getName());
        assertThat(activeIncidencesByDashboardName.iterator().next().getsTypeName()).isEqualTo(IssueType.BUG.getName());
    }

}
