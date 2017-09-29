package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "BuildType")
public class BuildEventHandler implements EventHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(BuildEventHandler.class);

    private final ConnectionHandler connectionHandler;
    private final DashboardService dashboardService;
    private final BuildService buildService;


    @Autowired
    public BuildEventHandler(ConnectionHandler connectionHandler, DashboardService dashboardService,
        BuildService buildService){

        this.connectionHandler = connectionHandler;
        this.dashboardService = dashboardService;
        this.buildService = buildService;
    }

    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {

        getBuildsForDashboard(dashboardIds);
    }

    private void getBuildsForDashboard(Set<String> dashboardIds) {
        dashboardIds.forEach(dashboardId -> {

            try {
                //Right now only build events are handled through emitter
                Map<String, Object> response = getBuildsForSingleDashboard(dashboardId);

                if (response != null && !response.isEmpty()) {
                    connectionHandler.sendMessageToDashboardSessions(response, dashboardId);
                }

            } catch (Exception unhandledException) {
                LOGGER.error("Unhandled exception calculating response of event", unhandledException);
            }
        });
    }

    private Map<String, Object> getBuildsForSingleDashboard(String dashboardId) {
        Map<String, Object> response = new HashMap<>();

        //Handle unchecked exception
        DashboardDTO dashboard = dashboardService.getDashboard(dashboardId);
        if (dashboard == null || dashboard.getCodeRepos() == null
            || dashboard.getCodeRepos().isEmpty()) {
            return null;
        }

        List<Build> builds = buildService
            .getLastBuildsByReposNameAndByTeamMembers(
                dashboard.getCodeRepos(), dashboard.getTeamMembers());
        BuildStats stats = buildService.getStatsFromReposByTeamMembers(
            dashboard.getCodeRepos(), dashboard.getTeamMembers());

        response.put("lastBuilds", builds);
        response.put("stats", stats);

        return response;
    }
}
