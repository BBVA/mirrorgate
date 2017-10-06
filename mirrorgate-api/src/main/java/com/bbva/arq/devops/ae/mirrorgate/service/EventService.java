package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import java.util.List;

public interface EventService {

    void saveBuildEvent(Build build);

    void saveFeatureEvent(Feature feature);

    void saveReviewEvents(Iterable<Review> reviews);

    Event getLastEvent();

    List<Event> getEventsSinceTimestamp(Long timestamp);

}
