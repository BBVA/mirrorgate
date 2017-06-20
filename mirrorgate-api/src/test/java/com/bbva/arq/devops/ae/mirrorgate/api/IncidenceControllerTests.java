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

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.FeatureService;
import com.bbva.arq.devops.ae.mirrorgate.util.TestObjectBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author enrique
 */
@RunWith(SpringRunner.class)
@WebMvcTest(IncidenceController.class)
@WebAppConfiguration
public class IncidenceControllerTests {

    private MockMvc mockMvc = null;

    @Autowired
    private WebApplicationContext wac;
    @MockBean
    private FeatureService featureService;
    @MockBean
    private DashboardService dashboardService;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getIncidencesTest() throws Exception {

        Dashboard dashboard = TestObjectBuilder.createDashboard();

        Feature inc1 = TestObjectBuilder.createIncidence();
        Feature inc2 = TestObjectBuilder.createIncidence();

        List<Feature> incidences = new ArrayList<>();
        incidences.add(inc1);
        incidences.add(inc2);

        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);
        when(featureService.getActiveIncidencesByBoards(Arrays.asList(dashboard.getsProductName())))
                .thenReturn(incidences);

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/incidences"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Incidences.nIncidences", equalTo(incidences.size())));
    }

}
