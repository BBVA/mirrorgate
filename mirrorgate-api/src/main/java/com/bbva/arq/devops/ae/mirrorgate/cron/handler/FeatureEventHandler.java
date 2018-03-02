package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.service.FeatureService;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component(value = "FeatureType")
public class FeatureEventHandler implements EventHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(FeatureEventHandler.class);

    private ConnectionHandler connectionHandler;
    private FeatureService featureService;
    private ProcessEventsHelper eventsHelper;


    @Autowired
    public FeatureEventHandler(ConnectionHandler connectionHandler,
        FeatureService featureService, ProcessEventsHelper eventsHelper){

        this.connectionHandler = connectionHandler;
        this.featureService = featureService;
        this.eventsHelper = eventsHelper;
    }

    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {

        List<ObjectId> idList = eventList.stream()
                                .map(Event::getEventTypeCollectionId)
                                .collect(Collectors.toList());

        if(idList.contains(null)) {
            connectionHandler.sendEventUpdateMessageToAll(EventType.FEATURE);
        } else {
            Iterable<Feature> features = featureService.getFeaturesByObjectId(idList);

            Predicate<Dashboard> filterDashboards = dashboard ->
                StreamSupport.stream(features.spliterator(), false)
                    .anyMatch(feature -> CollectionUtils.containsAny(feature.getKeywords(), dashboard.getBoards()));

            eventsHelper.processEvents(dashboardIds, filterDashboards, EventType.FEATURE);
        }
    }
}
