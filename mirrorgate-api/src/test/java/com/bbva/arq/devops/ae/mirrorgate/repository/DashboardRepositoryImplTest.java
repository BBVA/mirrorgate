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
