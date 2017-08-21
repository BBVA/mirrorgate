package com.bbva.arq.devops.ae.mirrorgate.cron;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ServerSideEventsHandler;
import com.bbva.arq.devops.ae.mirrorgate.connection.handler.SocketHandler;
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

    private ServerSideEventsHandler handler;

    private DashboardService dashboardService;


    @Autowired
    public EventScheduler(EventService eventService, BuildService buildService, ServerSideEventsHandler handler, DashboardService dashboardService){

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
                Map<String, Object> response = new HashMap<>();

                //Handle unchecked exception
                List<String> repos = dashboardService.getReposByDashboardName(dashboardId);
                List<Build> builds = buildService.getAllBranchesLastByReposName(repos);

                response.put("lastBuilds", builds);
                response.put("stats", buildService.getStatsFromRepos(repos));

                //Handle exceptions. When processing events, scheduler time should be set to the last event sent without errors
                handler.sendMessageToDashboardSessions(response, dashboardId);
            });

            //save last event timestamp to local variable
            schedulerTimestamp = unprocessedEvents.get(unprocessedEvents.size()-1).getTimestamp();
        }

        LOGGER.debug("Modified timestamp: {}", schedulerTimestamp);
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
