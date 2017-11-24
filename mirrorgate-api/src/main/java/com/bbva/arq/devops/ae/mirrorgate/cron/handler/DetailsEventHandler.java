/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component(value = "DashboardType")
public class DetailsEventHandler implements EventHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(DetailsEventHandler.class);

    private final ConnectionHandler connectionHandler;
    private DashboardService dashboardService;

    @Autowired
    public DetailsEventHandler(ConnectionHandler connectionHandler,
            DashboardService dashboardService){

        this.connectionHandler = connectionHandler;
        this.dashboardService = dashboardService;
    }

    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {
        List<Dashboard> dashboards = dashboardService.getDashboardWithNames(new ArrayList(dashboardIds));

        List<ObjectId> idList = eventList.stream()
                .map(Event::getEventTypeCollectionId)
                .collect(Collectors.toList());

        Map<String, List<String>> sourceAndTargets = new HashMap<>();
        dashboards.stream()
                .forEach((d) -> {
                    accumulateDashboard(sourceAndTargets, d, d.getName());
                    if(d.getAggregatedDashboards() != null) {
                        d.getAggregatedDashboards().forEach((ad) -> {
                            accumulateDashboard(sourceAndTargets, d, ad);
                        });
                    }
                });

        List<String> aggregatedNames = dashboards.stream()
                .filter((d) -> d.getAggregatedDashboards() != null)
                .flatMap((d) -> d.getAggregatedDashboards().stream())
                .distinct()
                .collect(Collectors.toList());
        if(aggregatedNames.size() > 0) {
            dashboards.addAll(dashboardService.getDashboardWithNames(aggregatedNames));
        }

        dashboards.stream().filter(d -> idList.contains(d.getId()))
                .flatMap((dashboard) -> sourceAndTargets.get(dashboard.getName()).stream())
                .distinct()
                .forEach(dName -> {
                    try {
                        connectionHandler.sendEventUpdateMessage(EventType.DETAIL, dName);
                    } catch (Exception unhandledException) {
                        LOGGER.error("Unhandled exception calculating response of event", unhandledException);
                    }
                });
    }

    private static void accumulateDashboard(Map<String, List<String>> sourceAndTargets, Dashboard d, String ad) {
        List<String> atarget = sourceAndTargets.get(ad);
        if(atarget == null) {
            atarget = new ArrayList<>();
            sourceAndTargets.put(ad, atarget);
        }
        atarget.add(d.getName());
    }
}
