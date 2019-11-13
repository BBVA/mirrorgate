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

import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.repository.IssueRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class IssueServiceTests {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private IssueServiceImpl issueService;

    @Test
    public void getActiveUserStoriesByProjectNameTest() {

        final DashboardDTO dashboard = TestObjectFactory.createDashboard();

        final Issue story1 = TestObjectFactory.createActiveStory();
        final Issue story2 = TestObjectFactory.createActiveStory();

        final List<Issue> stories = Arrays.asList(story1, story2);

        when(issueRepository.findActiveUserStoriesByBoards(
            Collections.singletonList(dashboard.getName()),
            Sort.by(Order.by("status")))
        ).thenReturn(stories);

        final List<Issue> activeStoriesByDashboardName
            = issueService.getActiveUserStoriesByBoards(Collections.singletonList(dashboard.getName()));

        verify(issueRepository, times(1)).findActiveUserStoriesByBoards(
            Collections.singletonList(dashboard.getName()), Sort.by(Order.by("status"))
        );

        assertThat(activeStoriesByDashboardName.get(0)).isEqualTo(story1);
        assertThat(activeStoriesByDashboardName.get(1)).isEqualTo(story2);
    }

    @Test
    public void testTransientDashboardIsCreated(){

        final IssueDTO story1 = TestObjectFactory.createIssueDTO(10L, "name1", "collector", "teamName1");
        final IssueDTO story2 = TestObjectFactory.createIssueDTO(11L, "name2", "collector", "teamName1");

        when(issueRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
        issueService.saveOrUpdateStories(Arrays.asList(story1, story2), "collector");

        verify(dashboardService, times(1)).createDashboardForJiraTeam(anyString());
    }

    @Test
    public void testTransientDashboardIsNotCreated(){

        final IssueDTO story1 = TestObjectFactory.createIssueDTO(10L, "name1", "collector");
        final IssueDTO story2 = TestObjectFactory.createIssueDTO(11L, "name2", "collector");

        when(issueRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
        issueService.saveOrUpdateStories(Arrays.asList(story1, story2), "collector");

        verify(dashboardService, times(0)).createDashboardForJiraTeam(anyString());
    }

}
