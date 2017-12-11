package com.bbva.arq.devops.ae.mirrorgate.api;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricUserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.HistoricUserMetricService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistoricUserMetricsController {

    private HistoricUserMetricService historicUserMetricService;
    private DashboardService dashboardService;

    @Autowired
    public HistoricUserMetricsController(HistoricUserMetricService historicUserMetricService,
                                         DashboardService dashboardService){

        this.historicUserMetricService = historicUserMetricService;
        this.dashboardService = dashboardService;
    }


    @RequestMapping(value = "/dashboards/{name}/historic-user-metrics", method = RequestMethod.GET)
    public List<HistoricUserMetricDTO> getHistoricUserMetric(@PathVariable("name") String name) {

        DashboardDTO dashboard = dashboardService.getDashboard(name);

        return historicUserMetricService.getHistoricMetricsForDashboard(dashboard, "requestNumbers", 24);
    }

}
