package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.BaseModel;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;

import java.util.List;

public interface EventService {

    void saveEvent(final BaseModel model, final EventType type);

    void saveEvents(final Iterable<? extends BaseModel> models, final EventType type);

    Event getLastEvent();

    List<Event> getEventsSinceTimestamp(final Long timestamp);

}
