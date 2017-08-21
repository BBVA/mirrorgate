package com.bbva.arq.devops.ae.mirrorgate.connection.handler;

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
public class ServerSideEventsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);

    private Map<String, List<SseEmitter>> emittersPerDashboard = new ConcurrentHashMap<>(1000);

    private ObjectMapper objectMapper;


    @Autowired
    public ServerSideEventsHandler(ObjectMapper objectMapper){

        this.objectMapper = objectMapper;
    }


    public Set<String> getDashboardsWithSession(){

        return Collections.unmodifiableSet(emittersPerDashboard.keySet());
    }

    public void sendMessageToDashboardSessions(Map<String, Object> stringMessage, String dashboardId) {

        List<SseEmitter> emitters = emittersPerDashboard.get(dashboardId);

        if(emitters != null){

            for(SseEmitter sseEmitter : emitters) {

                try {
                    String jsonMessage = objectMapper.writeValueAsString(stringMessage);
                    sseEmitter.send(jsonMessage, MediaType.APPLICATION_JSON);
                } catch (IOException e) {
                    LOGGER.error("Exception while sending message to emitter for dashboard {}", dashboardId);
                }

            }
        }
    }

    public synchronized void addToSessionsMap(SseEmitter session, String dashboardId) {

        List<SseEmitter> dashboardEmitters = emittersPerDashboard.get(dashboardId);

        if(dashboardEmitters == null){
            dashboardEmitters = new ArrayList<>();
        }

        dashboardEmitters.add(session);
        emittersPerDashboard.put(dashboardId, dashboardEmitters);
    }

    public synchronized void removeFromSessionsMap(SseEmitter session, String dashboardId){

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