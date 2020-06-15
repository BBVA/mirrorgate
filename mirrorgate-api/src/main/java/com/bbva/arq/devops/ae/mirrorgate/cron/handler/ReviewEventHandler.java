/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.service.ReviewService;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ReviewType")
public class ReviewEventHandler implements EventHandler {

    private final ReviewService reviewService;
    private final ProcessEventsHelper eventsHelper;

    @Autowired
    public ReviewEventHandler(final ReviewService reviewService, final ProcessEventsHelper eventsHelper) {
        this.reviewService = reviewService;
        this.eventsHelper = eventsHelper;
    }

    @Override
    public void processEvents(final List<Event> eventList, final Set<String> dashboardIds) {
        final List<ObjectId> idList = eventList.stream()
            .map(Event::getCollectionId)
            .filter(ObjectId.class::isInstance)
            .map(ObjectId.class::cast)
            .collect(Collectors.toList());

        final Iterable<Review> reviews = reviewService.getReviewsByObjectId(idList);

        final Predicate<Dashboard> filterDashboard = dashboard ->
            dashboard.getApplications() != null && ! dashboard.getApplications().isEmpty()
                && StreamSupport.stream(reviews.spliterator(), false)
                .anyMatch(review -> dashboard.getApplications().contains(review.getAppname()));

        eventsHelper.processEvents(dashboardIds, filterDashboard, EventType.REVIEW);
    }
}
