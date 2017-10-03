package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import java.util.List;
import java.util.Set;

public interface EventHandler {

    void processEvents(List<Event> eventList, Set<String> dashboardIds);
}
