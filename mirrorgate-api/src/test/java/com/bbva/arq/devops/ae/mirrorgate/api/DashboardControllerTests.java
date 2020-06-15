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

package com.bbva.arq.devops.ae.mirrorgate.api;

import static com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus.DELETED;
import static com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus.TRANSIENT;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardConflictException;
import com.bbva.arq.devops.ae.mirrorgate.exception.DashboardNotFoundException;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import com.bbva.arq.devops.ae.mirrorgate.support.TestUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebMvcTest(DashboardController.class)
@WebAppConfiguration
public class DashboardControllerTests {

    private MockMvc mockMvc;

    @Autowired private WebApplicationContext wac;
    @MockBean
    private DashboardService dashboardService;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();

        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/details"))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("name", equalTo(dashboard.getName())))
                .andExpect(jsonPath("logoUrl", equalTo(dashboard.getLogoUrl())));
    }

    @Test
    public void getNonexistentDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();

        doThrow(new DashboardNotFoundException(""))
            .when(dashboardService).getDashboard(dashboard.getName());

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/details"))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getDeletedDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();
        dashboard.setStatus(DELETED);

        doThrow(new DashboardNotFoundException(""))
            .when(dashboardService).getDashboard(dashboard.getName());

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/details"))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getActiveDashboardsTest() throws Exception {
        final DashboardDTO dashboard1 = TestObjectFactory.createDashboardDTO();
        final DashboardDTO dashboard2 = TestObjectFactory.createDashboardDTO();
        final List<DashboardDTO> dashboards = new ArrayList<>();
        dashboards.add(dashboard1);
        dashboards.add(dashboard2);

        when(dashboardService.getActiveDashboards()).thenReturn(dashboards);

        this.mockMvc.perform(get("/dashboards"))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(jsonPath("$[0].name", equalTo(dashboard1.getName())))
            .andExpect(jsonPath("$[0].logoUrl", equalTo(dashboard1.getLogoUrl())))
            .andExpect(jsonPath("$[1].name", equalTo(dashboard2.getName())))
            .andExpect(jsonPath("$[1].logoUrl", equalTo(dashboard2.getLogoUrl())));
    }

    @Test
    public void newDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();

        when(dashboardService.newDashboard(dashboard)).thenReturn(dashboard);

        this.mockMvc.perform(post("/dashboards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void newTransientDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();
        dashboard.setStatus(TRANSIENT);

        when(dashboardService.newTransientDashboard(dashboard)).thenReturn(dashboard);

        this.mockMvc.perform(post("/api/dashboards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void newPreviousDeletedDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();
        dashboard.setStatus(DELETED);

        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);
        when(dashboardService.updateDashboard(dashboard.getName(), dashboard)).thenReturn(dashboard);

        this.mockMvc.perform(post("/dashboards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void newPreviousCreatedDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();

        doThrow(new DashboardConflictException(""))
            .when(dashboardService).newDashboard(any());

        this.mockMvc.perform(post("/dashboards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }


    @Test
    public void deleteDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();

        doNothing().when(dashboardService).deleteDashboard(dashboard.getName());

        this.mockMvc.perform(delete("/dashboards/" + dashboard.getName()))
            .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void deleteWrongDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();

        doThrow(new DashboardNotFoundException(""))
            .when(dashboardService).deleteDashboard(dashboard.getName());

        this.mockMvc.perform(delete("/dashboards/" + dashboard.getName()))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void updateDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();

        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);
        when(dashboardService.updateDashboard(dashboard.getName(), dashboard)).thenReturn(dashboard);

        this.mockMvc.perform(put("/dashboards/" + dashboard.getName())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void updateWrongDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();
        final DashboardDTO wrongDashboard = TestObjectFactory.createDashboard();
        wrongDashboard.setName("Wrong");

        doThrow(new DashboardConflictException(""))
            .when(dashboardService).updateDashboard(anyString(), any(DashboardDTO.class));

        this.mockMvc.perform(put("/dashboards/" + dashboard.getName())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(wrongDashboard)))
            .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

    @Test
    public void updateNonexistentDashboardTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();

        doThrow(new DashboardNotFoundException(""))
            .when(dashboardService).updateDashboard(anyString(), any(DashboardDTO.class));

        this.mockMvc.perform(put("/dashboards/" + dashboard.getName())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dashboard)))
            .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}