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

import com.bbva.arq.devops.ae.mirrorgate.dto.BugDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.repository.IssueRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.BugPriority;
import com.bbva.arq.devops.ae.mirrorgate.support.BugStatus;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueStatus;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueType;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BugServiceTests {

    @Mock
    private IssueRepository issueRepository;
    @InjectMocks
    private BugServiceImpl bugService;

    @Test
    public void getActiveBugsByBoardsTest() {

        DashboardDTO dashboard = TestObjectFactory.createDashboard();

        Issue bug1 = TestObjectFactory.createBug();
        Issue bug2 = TestObjectFactory.createBug();

        List<Issue> bugs = new ArrayList<>();
        bugs.add(bug1);
        bugs.add(bug2);

        when(issueRepository.findByKeywordsInAndTypeAndStatusNot(
            Collections.singletonList(dashboard.getName()),
                IssueType.BUG.getName(),
                IssueStatus.DONE.getName())
        ).thenReturn(bugs);

        List<BugDTO> activeBugsByDashboardName
                = bugService.getActiveBugsByBoards(Collections.singletonList(dashboard.getName()));
        verify(issueRepository, times(1))
            .findByKeywordsInAndTypeAndStatusNot(
                    Collections.singletonList(dashboard.getName()),
                        IssueType.BUG.getName(),
                        IssueStatus.DONE.getName()
                );

        assertThat(activeBugsByDashboardName.get(0).getId()).isEqualTo(bug1.getNumber());
        assertThat(activeBugsByDashboardName.get(0).getPriority()).isEqualTo(BugPriority.fromName(bug1.getPriority()));
        assertThat(activeBugsByDashboardName.get(0).getStatus()).isNotEqualTo(BugStatus.DONE);
        assertThat(activeBugsByDashboardName.get(1).getId()).isEqualTo(bug2.getNumber());
        assertThat(activeBugsByDashboardName.get(1).getPriority()).isEqualTo(BugPriority.fromName(bug2.getPriority()));
        assertThat(activeBugsByDashboardName.get(1).getStatus()).isNotEqualTo(BugStatus.DONE);
    }
}
