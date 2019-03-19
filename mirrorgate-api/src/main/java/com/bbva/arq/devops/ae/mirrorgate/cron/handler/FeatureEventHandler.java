package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component(value = "FeatureType")
public class FeatureEventHandler implements EventHandler {

    private final ConnectionHandler connectionHandler;
    private final FeatureService featureService;
    private final ProcessEventsHelper eventsHelper;

    @Autowired
    public FeatureEventHandler(ConnectionHandler connectionHandler,
                               FeatureService featureService, ProcessEventsHelper eventsHelper) {

        this.connectionHandler = connectionHandler;
        this.featureService = featureService;
        this.eventsHelper = eventsHelper;
    }

    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {

        List<String> idList = eventList.stream()
            .map(Event::getEventTypeCollectionId)
            .map(String.class::cast)
            .collect(Collectors.toList());

        if (idList.contains(null)) {
            connectionHandler.sendEventUpdateMessageToAll(EventType.FEATURE);
        } else {
            Iterable<Feature> features = featureService.getFeaturesById(idList);

            Predicate<Dashboard> filterDashboards = dashboard ->
                StreamSupport.stream(features.spliterator(), false)
                    .anyMatch(feature -> CollectionUtils.containsAny(feature.getKeywords(), dashboard.getBoards()));

            eventsHelper.processEvents(dashboardIds, filterDashboards, EventType.FEATURE);
        }
    }
}
