package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
