package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component(value = "FeatureType")
public class FeatureEventHandler implements EventHandler {

    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {
        System.out.println(" Show Feature Event "+eventList);
    }
}
