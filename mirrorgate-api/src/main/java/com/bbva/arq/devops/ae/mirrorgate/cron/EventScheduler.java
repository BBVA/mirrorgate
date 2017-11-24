package com.bbva.arq.devops.ae.mirrorgate.cron;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.cron.handler.EventHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.service.EventService;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EventScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventScheduler.class);

    private final EventService eventService;

    private Long schedulerTimestamp = 0L;

    private final ConnectionHandler handler;

    private BeanFactory beanFactory;


    @Autowired
    public EventScheduler(EventService eventService,
        ConnectionHandler handler, BeanFactory beanFactory){

        this.eventService = eventService;
        this.handler = handler;
        this.beanFactory = beanFactory;
    }


    @Scheduled(fixedDelayString = "${events.scheduler.delay.millis}")
    public void checkEventUpdates() throws IOException {

        LOGGER.debug("Processing events for timestamp {}", schedulerTimestamp);

        Set<String> dashboardIds = handler.getDashboardsWithSession();

        if(dashboardIds != null){
            LOGGER.debug("Active dashboards {}", dashboardIds.size());
        }

        //process events
        if(!dashboardIds.isEmpty()) {
            //query DB for last events
            List<Event> unprocessedEvents = eventService.getEventsSinceTimestamp(schedulerTimestamp);
            if (!unprocessedEvents.isEmpty()) {

                //Filter events
                unprocessedEvents.stream()
                    .collect(Collectors.groupingBy(Event::getEventType))
                    .entrySet().stream()
                    .forEach(eventgroup ->
                        beanFactory.getBean(eventgroup.getKey().getValue(), EventHandler.class)
                            .processEvents(eventgroup.getValue(), dashboardIds)
                    );
                //save last event timestamp to local variable
                schedulerTimestamp = unprocessedEvents.get(unprocessedEvents.size() - 1).getTimestamp();
                LOGGER.debug("Modified timestamp: {}", schedulerTimestamp);
            }

        }

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
