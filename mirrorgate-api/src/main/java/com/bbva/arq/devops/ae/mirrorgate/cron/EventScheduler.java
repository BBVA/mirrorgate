package com.bbva.arq.devops.ae.mirrorgate.cron;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.EventService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EventScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventScheduler.class);

    private EventService eventService;

    private BuildService buildService;

    private Long schedulerTimestamp = 0L;

    private ConnectionHandler handler;

    private DashboardService dashboardService;


    @Autowired
    public EventScheduler(EventService eventService, BuildService buildService, ConnectionHandler handler, DashboardService dashboardService){

        this.eventService = eventService;
        this.buildService = buildService;
        this.handler = handler;
        this.dashboardService = dashboardService;
    }


    @Scheduled(fixedDelayString = "${events.scheduler.delay.millis}")
    public void checkEventUpdates() throws IOException {

        LOGGER.debug("Processing events for timestamp {}", schedulerTimestamp);

        //query DB for last events
        List<Event> unprocessedEvents = eventService.getEventsSinceTimestamp(schedulerTimestamp);

        //process events
        if(!unprocessedEvents.isEmpty()){

            Set<String> dashboardIds = handler.getDashboardsWithSession();

            dashboardIds.forEach(dashboardId -> {

                try {
                    //Right now only build events are handled through emitter
                    Map<String, Object> response = getBuildsForDashboard(dashboardId);

                    if (response != null && !response.isEmpty()) {
                        handler.sendMessageToDashboardSessions(response, dashboardId);
                    }

                } catch(Exception unhandledException) {
                    LOGGER.error("Unhandled exception calculating response of event", unhandledException);
                }
            });

            //save last event timestamp to local variable
            schedulerTimestamp = unprocessedEvents.get(unprocessedEvents.size()-1).getTimestamp();
        }

        LOGGER.debug("Modified timestamp: {}", schedulerTimestamp);
    }

    private Map<String, Object> getBuildsForDashboard(String dashboardId) {
        Map<String, Object> response = new HashMap<>();

        //Handle unchecked exception
        List<String> repos = dashboardService.getReposByDashboardName(dashboardId);

        if(repos != null && !repos.isEmpty()) {
            List<Build> builds = buildService.getAllBranchesLastByReposName(repos);

            response.put("lastBuilds", builds);
            response.put("stats", buildService.getStatsFromRepos(repos));

        }
        return response;
    }

    @PostConstruct
    private void initSchedulerTimestamp(){

        Event lastEvent = eventService.getLastEvent();

        if(lastEvent != null) {
            schedulerTimestamp = lastEvent.getTimestamp();
        }

        LOGGER.info("Scheduler initial timestamp set to {}", schedulerTimestamp);
    }

}
