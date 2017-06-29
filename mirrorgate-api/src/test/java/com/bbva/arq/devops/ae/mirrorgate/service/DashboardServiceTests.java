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

import static com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus.DELETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import com.bbva.arq.devops.ae.mirrorgate.util.TestObjectBuilder;
import com.bbva.arq.devops.ae.mirrorgate.utils.MirrorGateException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private static final Sort SORT_BY_LAST_MODIFICATION = new Sort(Sort.Direction.DESC, "lastModification");

    @Before
    public void before() {
        when(auth.getPrincipal()).thenReturn(TestObjectBuilder.AUTH_NAME);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void getDashboardByNameTest() throws MirrorGateException {
        Dashboard dashboard = TestObjectBuilder.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(dashboard);

        Dashboard dashboard2 = dashboardService.getDashboard(dashboard.getName());
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);

        assertThat(dashboard2.getId()).isEqualTo(dashboard.getId());
        assertThat(dashboard2.getName()).isEqualTo(dashboard.getName());
        assertThat(dashboard2.getLogoUrl()).isEqualTo(dashboard.getLogoUrl());
    }

    @Test
    public void getReposByDashboardNameTest() throws MirrorGateException {
        Dashboard dashboard = TestObjectBuilder.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(dashboard);

        List<String> codeReposByDashboardName = dashboardService.getReposByDashboardName(dashboard.getName());
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);

        assertThat(codeReposByDashboardName.get(0)).isEqualTo(dashboard.getCodeRepos().get(0));
        assertThat(codeReposByDashboardName.get(1)).isEqualTo(dashboard.getCodeRepos().get(1));
    }

    @Test
    public void getActiveDashboardsTest() {
        DashboardDTO dashboard1 = TestObjectBuilder.createDashboardDTO();
        DashboardDTO dashboard2 = TestObjectBuilder.createDashboardDTO();
        List<DashboardDTO> dashboards = new ArrayList<>();
        dashboards.add(dashboard1);
        dashboards.add(dashboard2);

        when(dashboardRepository.getActiveDashboards()).thenReturn(dashboards);

        List<DashboardDTO> dashboardsList = dashboardService.getActiveDashboards();
        verify(dashboardRepository, times(1)).getActiveDashboards();

        assertThat(dashboardsList).isEqualTo(dashboards);
    }

    @Test
    public void newDashboardTest() throws MirrorGateException {
        Dashboard dashboard = TestObjectBuilder.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(null);
        when(dashboardRepository.save(dashboard)).thenReturn(dashboard);

        Dashboard dashboard2 = dashboardService.newDashboard(dashboard);
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);
        verify(dashboardRepository, times(1)).save(dashboard);

        assertThat(dashboard2.getName()).isEqualTo(dashboard.getName());
        assertThat(dashboard2.getName()).isEqualTo(dashboard.getName());
    }

    @Test(expected = MirrorGateException.class)
    public void newPreviusCreatedDashboardTest() throws MirrorGateException {
        Dashboard dashboard = TestObjectBuilder.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(dashboard);

        dashboardService.newDashboard(dashboard);
    }

    @Test(expected = MirrorGateException.class)
    public void newPreviusDeletedDashboardTest() throws MirrorGateException {
        Dashboard dashboard = TestObjectBuilder.createDashboard();
        dashboard.setStatus(DELETED);

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(dashboard);

        dashboardService.newDashboard(dashboard);
    }

    @Test
    public void updateDashboardTest() throws MirrorGateException {
        Dashboard dashboard = TestObjectBuilder.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(dashboard);
        when(dashboardRepository.save(dashboard)).thenReturn(dashboard);

        Dashboard dashboard2 = dashboardService.updateDashboard(dashboard);
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);
        verify(dashboardRepository, times(1)).save(dashboard);

        assertThat(dashboard2.getName()).isEqualTo(dashboard.getName());
        assertThat(dashboard2.getName()).isEqualTo(dashboard.getName());
    }

    @Test(expected = MirrorGateException.class)
    public void updateWrongDashboardTest() throws MirrorGateException {
        Dashboard dashboard = TestObjectBuilder.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(null);

        dashboardService.updateDashboard(dashboard);
    }

    @Test
    public void deleteDashboardTest() throws MirrorGateException {
        Dashboard dashboard = TestObjectBuilder.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(dashboard);
        when(dashboardRepository.save(dashboard)).thenReturn(dashboard);

        dashboardService.deleteDashboard(dashboard.getName());
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);
        verify(dashboardRepository, times(1)).save(dashboard);
    }

    @Test(expected = MirrorGateException.class)
    public void deleteWrongDashboardTest() throws MirrorGateException {
        Dashboard dashboard = TestObjectBuilder.createDashboard();

        when(dashboardRepository.findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION)).thenReturn(null);

        dashboardService.deleteDashboard(dashboard.getName());
        verify(dashboardRepository, times(1)).findOneByName(dashboard.getName(), SORT_BY_LAST_MODIFICATION);
    }

    @After
    public void after() {
        SecurityContextHolder.clearContext();
    }
}
