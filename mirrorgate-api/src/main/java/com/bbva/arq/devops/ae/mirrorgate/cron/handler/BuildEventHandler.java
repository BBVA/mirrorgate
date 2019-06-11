package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component(value = "BuildType")
public class BuildEventHandler implements EventHandler {

    private final ConnectionHandler connectionHandler;
    private final BuildService buildService;
    private final ProcessEventsHelper eventsHelper;

    @Autowired
    BuildEventHandler(ConnectionHandler connectionHandler, BuildService buildService, ProcessEventsHelper eventsHelper) {

        this.connectionHandler = connectionHandler;
        this.buildService = buildService;
        this.eventsHelper = eventsHelper;
    }

    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {
        List<String> ids = eventList.stream()
            .map(Event::getCollectionId)
            .map(String.class::cast)
            .collect(Collectors.toList());

        if (ids.contains(null)) {
            connectionHandler.sendEventUpdateMessageToAll(EventType.BUILD);
        } else {
            Iterable<BuildDTO> builds = buildService.getBuildsById(ids);

            Predicate<Dashboard> filterDashboards = dashboard ->
                StreamSupport
                    .stream(builds.spliterator(), false)
                    .anyMatch(build -> {
                        if (dashboard.getTeamMembers() != null && !dashboard.getTeamMembers().isEmpty() && !CollectionUtils.containsAny(dashboard.getTeamMembers(), build.getCulprits())) {
                            return false;
                        }
                        return dashboard.getCodeRepos().stream().anyMatch(repo ->
                            build.getKeywords()
                                .stream()
                                .anyMatch(keyword -> Pattern.matches("^" + repo + "$", keyword))
                        );
                    });

            eventsHelper.processEvents(dashboardIds, filterDashboards, EventType.BUILD);
        }
    }
}
