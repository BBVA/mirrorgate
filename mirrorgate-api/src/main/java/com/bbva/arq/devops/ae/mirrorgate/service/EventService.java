package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import java.util.List;

public interface EventService {

    void saveBuildEvent(Build build);

    Event getLastEvent();

    List<Event> getEventsSinceTimestamp(Long timestamp);

}
