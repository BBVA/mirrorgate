package com.bbva.arq.devops.ae.mirrorgate.api;

import com.bbva.arq.devops.ae.mirrorgate.service.ProgramIncrementService;
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

@RunWith(SpringRunner.class)
@WebMvcTest(ProgramIncrementController.class)
@WebAppConfiguration
public class ProgramIncrementControllerTest {

    private MockMvc mockMvc = null;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private ProgramIncrementService programIncrementService;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testDashboardNotFoundException() throws Exception {

//        when(programIncrementService.getProgramIncrementFeatures(anyString())).thenThrow(
//            new DashboardNotFoundExcepion("dashboard1"));
//
//        MvcResult result = this.mockMvc.perform(get("/dashboards/mirrorgate/programincrement"))
//            .andReturn();

    }

}
