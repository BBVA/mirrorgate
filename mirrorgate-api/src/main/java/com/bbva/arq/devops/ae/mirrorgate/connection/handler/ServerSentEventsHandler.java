package com.bbva.arq.devops.ae.mirrorgate.connection.handler;

import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSentEventsHandler.class);

    private Map<String, List<SseEmitter>> emittersPerDashboard = new ConcurrentHashMap<>(1000);

    private ObjectMapper objectMapper;


    @Autowired
    public ServerSentEventsHandler(ObjectMapper objectMapper){

        this.objectMapper = objectMapper;
    }


    @Override
    public Set<String> getDashboardsWithSession(){

        return Collections.unmodifiableSet(emittersPerDashboard.keySet());
    }

    @Override
    public void sendEventUpdateMessage(EventType event, String dashboardId) {

        List<SseEmitter> emitters = emittersPerDashboard.get(dashboardId);

        if(emitters != null){

            if(event != EventType.PING) {
                sendEventUpdateMessage(EventType.PING, dashboardId);
            }

            LOGGER.info("Notifying {} dashboards with name {} and event type {}", emitters.size(), dashboardId, event);

            for(int i = emitters.size(); i > 0; i--) {
                SseEmitter sseEmitter = emitters.get(i-1);

                try {
                    Map<String, String> message = new HashMap<>();
                    message.put("type", event.getValue());
                    String jsonMessage = objectMapper.writeValueAsString(message);
                    sseEmitter.send(jsonMessage, MediaType.APPLICATION_JSON);
                } catch (IOException e) {
                    this.removeFromSessionsMap(sseEmitter, dashboardId);
                    LOGGER.error("Exception while sending message to emitter for dashboard {}", dashboardId);
                }

            }

        }
    }

    @Override
    public void sendEventUpdateMessageToAll(EventType event) {

        emittersPerDashboard.keySet().forEach(dashboardId -> sendEventUpdateMessage(event, dashboardId));
    }

    public synchronized void addToSessionsMap(SseEmitter session, String dashboardId) {

        LOGGER.debug("Add SseEmitter {} to sessions map", dashboardId);

        List<SseEmitter> dashboardEmitters = emittersPerDashboard.get(dashboardId);

        if(dashboardEmitters == null){
            dashboardEmitters = new ArrayList<>();
            emittersPerDashboard.put(dashboardId, dashboardEmitters);
        }

        dashboardEmitters.add(session);
    }

    public synchronized void removeFromSessionsMap(SseEmitter session, String dashboardId){

        LOGGER.debug("Remove SseEmitter {} to sessions map", dashboardId);

        if(!StringUtils.isEmpty(dashboardId)){
            List<SseEmitter> dashboardEmitters = emittersPerDashboard.get(dashboardId);

            if(dashboardEmitters != null){
                dashboardEmitters.remove(session);

                if(dashboardEmitters.isEmpty()){
                    emittersPerDashboard.remove(dashboardId);
                }
            }
        }
    }

}