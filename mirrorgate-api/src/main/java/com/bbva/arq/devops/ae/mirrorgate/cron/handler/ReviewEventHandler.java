package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.ReviewService;
import java.util.ArrayList;
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

@Component("ReviewType")
public class ReviewEventHandler implements EventHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(FeatureEventHandler.class);

    private final ReviewService reviewService;
    private final ProcessEventsHelper eventsHelper;


    @Autowired
    public ReviewEventHandler(ReviewService reviewService, ProcessEventsHelper eventsHelper){

        this.reviewService = reviewService;
        this.eventsHelper = eventsHelper;
    }


    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {

        List<ObjectId> idList = eventList.stream()
            .map(Event::getEventTypeCollectionId).collect(Collectors.toList());

        Iterable<Review> reviews = reviewService.getReviewsByObjectId(idList);

        Predicate<Dashboard> filterDashboard = dashboard ->
                    dashboard.getApplications() != null && !dashboard.getApplications().isEmpty()
                        && StreamSupport.stream(reviews.spliterator(), false)
                    .anyMatch(review -> dashboard.getApplications().contains(review.getAppname()));

        eventsHelper.processEvents(dashboardIds, filterDashboard, EventType.REVIEW);

    }
}
