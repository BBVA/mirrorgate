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
import com.bbva.arq.devops.ae.mirrorgate.dto.BuildDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.service.BuildService;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component("BuildType")
public class BuildEventHandler implements EventHandler {

    private final ConnectionHandler connectionHandler;
    private final BuildService buildService;
    private final ProcessEventsHelper eventsHelper;

    @Autowired
    public BuildEventHandler(
        final ConnectionHandler connectionHandler,
        final BuildService buildService,
        final ProcessEventsHelper eventsHelper
    ) {
        this.connectionHandler = connectionHandler;
        this.buildService = buildService;
        this.eventsHelper = eventsHelper;
    }

    @Override
    public void processEvents(final List<Event> eventList, final Set<String> dashboardIds) {
        final List<String> ids = eventList.stream()
            .map(Event::getCollectionId)
            .map(String.class::cast)
            .collect(Collectors.toList());

        if (ids.contains(null)) {
            connectionHandler.sendEventUpdateMessageToAll(EventType.BUILD);
        } else {
            final Iterable<BuildDTO> builds = buildService.getBuildsByIds(ids);

            final Predicate<Dashboard> filterDashboards = dashboard ->
                StreamSupport
                    .stream(builds.spliterator(), false)
                    .anyMatch(build -> {

                        if (dashboard.getCodeRepos() == null || dashboard.getCodeRepos().isEmpty()) {
                            return false;
                        }

                        if (dashboard.getTeamMembers() != null && ! dashboard.getTeamMembers().isEmpty()
                            && ! CollectionUtils.containsAny(dashboard.getTeamMembers(), build.getCulprits())) {
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
