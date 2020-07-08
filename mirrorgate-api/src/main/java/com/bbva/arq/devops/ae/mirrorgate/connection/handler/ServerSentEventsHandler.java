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

package com.bbva.arq.devops.ae.mirrorgate.connection.handler;

import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class ServerSentEventsHandler implements ConnectionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ServerSentEventsHandler.class);

    private final Map<String, List<SseEmitter>> emittersPerDashboard = new ConcurrentHashMap<>(1000);

    private final ObjectMapper objectMapper;


    @Autowired
    public ServerSentEventsHandler(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public Set<String> getDashboardsWithSession() {
        return Collections.unmodifiableSet(emittersPerDashboard.keySet());
    }

    @Override
    public void sendEventUpdateMessage(final EventType event, final String dashboardId) {

        final List<SseEmitter> emitters = emittersPerDashboard.get(dashboardId);

        if (emitters != null) {

            if (event != EventType.PING) {
                sendEventUpdateMessage(EventType.PING, dashboardId);
            }

            LOG.info("Notifying {} dashboards with name {} and event type {}", emitters.size(), dashboardId, event);

            for (int i = emitters.size(); i > 0; i--) {
                final SseEmitter sseEmitter = emitters.get(i - 1);

                try {
                    final String jsonMessage = objectMapper.writeValueAsString(
                        Map.of("type", event.getValue())
                    );
                    sseEmitter.send(jsonMessage, MediaType.APPLICATION_JSON);
                } catch (IOException e) {
                    this.removeFromSessionsMap(sseEmitter, dashboardId);
                    LOG.error("Exception while sending message to emitter for dashboard {}", dashboardId);
                }
            }
        }
    }

    @Override
    public void sendEventUpdateMessageToAll(final EventType event) {

        emittersPerDashboard.keySet().forEach(dashboardId -> sendEventUpdateMessage(event, dashboardId));
    }

    public synchronized void addToSessionsMap(final SseEmitter session, final String dashboardId) {

        LOG.debug("Add SseEmitter {} to sessions map", dashboardId);

        final List<SseEmitter> dashboardEmitters = emittersPerDashboard
            .computeIfAbsent(dashboardId, k -> new ArrayList<>());

        dashboardEmitters.add(session);
    }

    public synchronized void removeFromSessionsMap(final SseEmitter session, final String dashboardId) {

        LOG.debug("Remove SseEmitter {} to sessions map", dashboardId);

        if (! StringUtils.isEmpty(dashboardId)) {
            final List<SseEmitter> dashboardEmitters = emittersPerDashboard.get(dashboardId);

            if (dashboardEmitters != null) {
                dashboardEmitters.remove(session);

                if (dashboardEmitters.isEmpty()) {
                    emittersPerDashboard.remove(dashboardId);
                }
            }
        }
    }

}