package com.bbva.arq.devops.ae.mirrorgate.cron;

import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import com.bbva.arq.devops.ae.mirrorgate.service.EventService;
import com.bbva.arq.devops.ae.mirrorgate.websocket.SocketHandler;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.bson.types.ObjectId;
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

    private SocketHandler socketHandler;


    @Autowired
    public EventScheduler(EventService eventService, BuildService buildService, SocketHandler socketHandler){

        this.eventService = eventService;
        this.buildService = buildService;
        this.socketHandler = socketHandler;
    }


    @Scheduled(fixedDelayString = "${events.scheduler.delay.millis}")
    public void checkEventUpdates() throws IOException {

        LOGGER.debug("Processing events for timestamp {}", schedulerTimestamp);

        //query DB for last events
        List<Event> unprocessedEvents = eventService.getEventsSinceTimestamp(schedulerTimestamp);

        //process events
        if(!unprocessedEvents.isEmpty()){

            List<ObjectId> unprocessedBuildsId =
                unprocessedEvents.stream()
                    .map(Event::getEventTypeCollectionId)
                    .collect(Collectors.toList());

            List<Build> builds = buildService.getAllBuildsFromId(unprocessedBuildsId);

            builds.forEach(
                b -> LOGGER.debug("Processing build {} ", b.getBuildUrl()));

            //Handle exceptions. When processing events, scheduler time should be set to the last event sent without errors
            socketHandler.broadcastMessage("message from the cron scheduler");

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
