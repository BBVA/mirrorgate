/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.repository;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataMongoTest
public class DashboardRepositoryImplTest {

    @Autowired
    private DashboardRepository dashboardRepository;


    @Before
    public void init(){

        Dashboard regularDashboard = new Dashboard();
        regularDashboard.setName("regularDashboard");
        regularDashboard.setStatus(DashboardStatus.ACTIVE);

        Dashboard nullStatusDashboard = new Dashboard();
        nullStatusDashboard.setName("nullStatusDashboard");

        Dashboard transientDashboard = new Dashboard();
        transientDashboard.setName("transientDashboard");
        transientDashboard.setStatus(DashboardStatus.TRANSIENT);


        Dashboard deletedDashboard = new Dashboard();
        deletedDashboard.setName("deletedDashboard");
        deletedDashboard.setStatus(DashboardStatus.DELETED);

        dashboardRepository.save(Arrays.asList(regularDashboard, nullStatusDashboard, transientDashboard, deletedDashboard));
    }

    @After
    public void clean(){
        dashboardRepository.deleteAll();
    }

    @Test
    public void transientAndDeletedDashboardsAreNotReturnedTest(){
        List<Dashboard> activeDashboards = dashboardRepository.getActiveDashboards();

        List<String> dashboardNames = activeDashboards.stream().map(Dashboard::getName).collect(Collectors.toList());

        assertTrue(dashboardNames.contains("regularDashboard"));
        assertTrue(dashboardNames.contains("nullStatusDashboard"));
        assertEquals(activeDashboards.size(), 2);
    }
}
