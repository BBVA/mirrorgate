package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.FeatureService;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    private final ConnectionHandler connectionHandler;
    private final DashboardService dashboardService;
    private final FeatureService featureService;


    @Autowired
    public FeatureEventHandler(ConnectionHandler connectionHandler, DashboardService dashboardService,
        FeatureService featureService){

        this.connectionHandler = connectionHandler;
        this.dashboardService = dashboardService;
        this.featureService = featureService;
    }

    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {

        List<Dashboard> dashboards = dashboardService.getDashboardWithNames(new ArrayList(dashboardIds));

        List<ObjectId> idList = eventList.stream()
                                .map(Event::getEventTypeCollectionId).collect(Collectors.toList());
        Iterable<Feature> features = featureService.getFeaturesByObjectId(idList);

        dashboards.forEach(dashboard -> {
            //check if there is a feature changed
            Optional<Feature> featureCheck = StreamSupport.stream(features.spliterator(), false)
                .filter(feature -> CollectionUtils.containsAny(feature.getKeywords(), dashboard.getBoards())).findFirst();
            if(featureCheck.isPresent()){
                //Send update message to dashboard
                connectionHandler.sendEventUpdateMessage(EventType.FEATURE, dashboard.getName());
            }
        });
    }
}
