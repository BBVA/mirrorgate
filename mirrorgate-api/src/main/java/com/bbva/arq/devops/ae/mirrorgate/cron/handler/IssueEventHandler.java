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

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.service.IssueService;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component("IssueType")
public class IssueEventHandler implements EventHandler {

    private final ConnectionHandler connectionHandler;
    private final IssueService issueService;
    private final ProcessEventsHelper eventsHelper;

    @Autowired
    public IssueEventHandler(
        final ConnectionHandler connectionHandler,
        final IssueService issueService,
        final ProcessEventsHelper eventsHelper
    ) {
        this.connectionHandler = connectionHandler;
        this.issueService = issueService;
        this.eventsHelper = eventsHelper;
    }

    @Override
    public void processEvents(final List<Event> eventList, final Set<String> dashboardIds) {

        final List<String> idList = eventList.stream()
            .map(Event::getCollectionId)
            .map(String.class::cast)
            .collect(Collectors.toList());

        if (idList.contains(null)) {
            connectionHandler.sendEventUpdateMessageToAll(EventType.ISSUE);
        } else {
            final Iterable<Issue> issues = issueService.getIssuesById(idList);

            final Predicate<Dashboard> filterDashboards = dashboard ->
                StreamSupport.stream(issues.spliterator(), false)
                    .anyMatch(issue -> CollectionUtils.containsAny(issue.getKeywords(), dashboard.getBoards()));

            eventsHelper.processEvents(dashboardIds, filterDashboards, EventType.ISSUE);
        }
    }
}
