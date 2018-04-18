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

import static junit.framework.TestCase.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.exception.MirrorGateException;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.repository.DashboardRepository;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class DashboardServiceTestsIT {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private DashboardRepository dashboardRepository;

    @Test
    public void testMongo() throws MirrorGateException {
        Dashboard dashboard = new Dashboard()
                .setName("mirrorgate")
                .setsProductName("mirrorgate")
                .setApplications(Collections.singletonList("mirrorgate"));

        dashboardRepository.save(dashboard);

        List<String> dashboardFromMongo = dashboardService.getApplicationsByDashboardName("mirrorgate");

        assertTrue(dashboardFromMongo.contains("mirrorgate"));

    }

    @After
    public void cleanUp() {
        dashboardRepository.deleteAll();
    }
}
