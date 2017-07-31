package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.repository.EventRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService{

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

    private EventRepository eventRepository;

    private Long schedulerTimestamp = 0L;


    @Autowired
    public EventServiceImpl(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }


    @Override
    public void saveBuildEvent(Build build) {
        LOGGER.info("Saving build with Id :{}", build.getId());

        Event buildEvent = new Event();

        buildEvent.setEventType(EventType.BUILD);
        buildEvent.setEventTypeCollectionId(build.getId());
        buildEvent.setTimestamp(System.currentTimeMillis());

        eventRepository.save(buildEvent);
    }

    @Scheduled(fixedDelayString = "2000")
    public void checkEventUpdates(){

        LOGGER.info("Processing events for timestamp {}", schedulerTimestamp);

        //query DB for last events
        List<Event> unprocessedEvents = eventRepository.findByTimestampGreaterThanOrderByTimestampAsc(schedulerTimestamp);

        //process events
        if(!unprocessedEvents.isEmpty()){

            unprocessedEvents.forEach(
                e -> LOGGER.debug("Processing event {} of type {}", e.getEventTypeCollectionId(), e.getEventType()));

            //save last event timestamp to local variable
            schedulerTimestamp = unprocessedEvents.get(unprocessedEvents.size()-1).getTimestamp();
        }
    }

    @PostConstruct
    private void initSchedulerTimestamp(){
        LOGGER.info("Setting events scheduler timestamp");
        Event lastEvent = eventRepository.findFirstByOrderByTimestampDesc();

        if(lastEvent != null) {
            schedulerTimestamp = lastEvent.getTimestamp();
        }
        LOGGER.info("Scheduler timestamp set to {}", schedulerTimestamp);
    }
}
