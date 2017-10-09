package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.BaseModel;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.repository.EventRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Service
public class EventServiceImpl implements EventService{

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

    private EventRepository eventRepository;


    @Autowired
    public EventServiceImpl(EventRepository eventRepository){

        this.eventRepository = eventRepository;
    }

    @Override
    public void saveEvent(BaseModel baseObject, EventType type){

        LOGGER.info("Saving event with Id :{}", baseObject.getId());

        try{
            Event platformEvent = new Event();

            platformEvent.setEventType(type);
            platformEvent.setEventTypeCollectionId(baseObject.getId());
            platformEvent.setTimestamp(System.currentTimeMillis());

            eventRepository.save(platformEvent);
        } catch (Exception e){
            LOGGER.error("Error while saving event", e);
        }
    }

    @Override
    public void saveEvents(Iterable<? extends BaseModel> reviews, EventType type) {
        LOGGER.info("Saving list of events");

        try{
            List<Event> eventList = StreamSupport.stream(reviews.spliterator(), false)
                .map(review -> {
                    Event platformEvent = new Event();

                    platformEvent.setEventType(type);
                    platformEvent.setEventTypeCollectionId(review.getId());
                    platformEvent.setTimestamp(System.currentTimeMillis());

                    return platformEvent;
                }).collect(Collectors.toList());

            eventRepository.save(eventList);
        } catch (Exception e){
            LOGGER.error("Error while saving event", e);
        }
    }

    @Override
    public Event getLastEvent(){
        return eventRepository.findFirstByOrderByTimestampDesc();
    }

    @Override
    public List<Event> getEventsSinceTimestamp(Long timestamp){
        return eventRepository.findByTimestampGreaterThanOrderByTimestampAsc(timestamp);
    }
}
