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

import static com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus.ACTIVE;
import static com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus.TRANSIENT;
import static com.bbva.arq.devops.ae.mirrorgate.mapper.DashboardMapper.map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardConflictException;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardNotFoundException;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class DashboardServiceTests {

    @Mock
    private Authentication auth;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private DashboardRepository dashboardRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private static final Sort SORT_BY_LAST_MODIFICATION = new Sort(Sort.Direction.DESC, "lastModification");

    @Before
    public void before() {
        when(auth.getPrincipal()).thenReturn(TestObjectFactory.AUTH_NAME);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void getDashboardByNameTest() {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(map(dashboard));
        when(dashboardRepository.save(any(Dashboard.class))).thenReturn(map(dashboard));

        DashboardDTO dashboard2 = dashboardService.getDashboard(dashboard.getName());
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);

        assertThat(dashboard2.getName()).isEqualTo(dashboard.getName());
        assertThat(dashboard2.getLogoUrl()).isEqualTo(dashboard.getLogoUrl());
    }

    @Test
    public void getReposByDashboardNameTest() {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(map(dashboard));
        when(dashboardRepository.save(any(Dashboard.class))).thenReturn(map(dashboard));

        List<String> codeReposByDashboardName = dashboardService.getReposByDashboardName(dashboard.getName());
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);

        assertThat(codeReposByDashboardName.get(0)).isEqualTo(dashboard.getCodeRepos().get(0));
        assertThat(codeReposByDashboardName.get(1)).isEqualTo(dashboard.getCodeRepos().get(1));
    }

    @Test
    public void newDashboardTest() {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(null);
        when(dashboardRepository.save((Dashboard) any())).thenReturn(map(dashboard));

        DashboardDTO dashboard2 = dashboardService.newDashboard(dashboard);
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);
        verify(dashboardRepository, times(1)).save((Dashboard) any());

        assertThat(dashboard2.getName()).isEqualTo(dashboard.getName());
        assertThat(dashboard2.getName()).isEqualTo(dashboard.getName());
    }

    @Test(expected = DashboardConflictException.class)
    public void newPreviousCreatedDashboardTest() {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(map(dashboard));

        dashboardService.newDashboard(dashboard);
    }

    @Test
    public void updateDashboardAdminUserTest() {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(map(dashboard));
        when(dashboardRepository.save((Dashboard) any())).thenAnswer(d -> d.getArguments()[0]);

        DashboardDTO dashboard2 = dashboardService.updateDashboard(dashboard.getName(), dashboard);
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);
        verify(dashboardRepository, times(1)).save((Dashboard) any());

        assertThat(dashboard2.getAdminUsers()).contains(TestObjectFactory.AUTH_NAME);
    }

    @Test
    public void updateTransientDashboard() {

        ArgumentCaptor<Dashboard> argument
                = ArgumentCaptor.forClass(Dashboard.class);

        DashboardDTO dashboard = TestObjectFactory.createDashboard();
        Dashboard rDashboard = map(dashboard);
        rDashboard.setStatus(TRANSIENT);

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(rDashboard);
        when(dashboardRepository.save((Dashboard) any())).thenAnswer(d -> d.getArguments()[0]);

        DashboardDTO dashboard2 = dashboardService.updateDashboard(dashboard.getName(), dashboard);
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);
        verify(dashboardRepository, times(1)).save((Dashboard) any());
        verify(dashboardRepository).save(argument.capture());

        assertThat(argument.getValue().getStatus()).isEqualTo(ACTIVE);
        assertThat(dashboard2.getAdminUsers()).contains(TestObjectFactory.AUTH_NAME);
    }

    @Test(expected = DashboardNotFoundException.class)
    public void updateWrongDashboardTest() {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(null);

        dashboardService.updateDashboard(dashboard.getName(),dashboard);
    }

    @Test
    public void deleteDashboardTest() {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(map(dashboard));
        when(dashboardRepository.save(map(dashboard))).thenReturn(map(dashboard));

        dashboardService.deleteDashboard(dashboard.getName());
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);
        verify(dashboardRepository, times(1)).save((Dashboard) any());
    }

    @Test(expected = DashboardNotFoundException.class)
    public void deleteNotFoundDashboardTest() {
        DashboardDTO dashboard = TestObjectFactory.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(null);

        dashboardService.deleteDashboard(dashboard.getName());
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);
    }

    @After
    public void after() {
        SecurityContextHolder.clearContext();
    }
}
