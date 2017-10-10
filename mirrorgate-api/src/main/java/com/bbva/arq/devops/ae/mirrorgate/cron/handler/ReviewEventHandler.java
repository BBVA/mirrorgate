package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.ReviewService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    private final ConnectionHandler connectionHandler;
    private final DashboardService dashboardService;
    private final ReviewService reviewService;


    @Autowired
    public ReviewEventHandler(ConnectionHandler connectionHandler, DashboardService dashboardService,
        ReviewService reviewService){

        this.connectionHandler = connectionHandler;
        this.dashboardService = dashboardService;
        this.reviewService = reviewService;
    }


    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {

        List<Dashboard> dashboards = dashboardService.getDashboardWithNames(new ArrayList(dashboardIds));

        List<ObjectId> idList = eventList.stream()
            .map(Event::getEventTypeCollectionId).collect(Collectors.toList());
        Iterable<Review> reviews = reviewService.getReviewsByObjectId(idList);

        dashboards.forEach(dashboard -> {
            //check if there is a feature changed
            if(dashboard.getApplications() != null && !dashboard.getApplications().isEmpty()){
                Optional<Review> reviewCheck = StreamSupport.stream(reviews.spliterator(), false)
                    .filter(review -> dashboard.getApplications().contains(review.getAppname()))
                    .findFirst();
                if(reviewCheck.isPresent()){
                    //Send update message to dashboard
                    connectionHandler.sendEventUpdateMessage(EventType.REVIEW, dashboard.getName());
                }
            }

        });
    }
}
