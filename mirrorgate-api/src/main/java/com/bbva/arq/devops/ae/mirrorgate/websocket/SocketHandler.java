package com.bbva.arq.devops.ae.mirrorgate.websocket;

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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);

    private Map<String, List<WebSocketSession>> sessionsPerDashboard = new ConcurrentHashMap<>(1000);

    private ObjectMapper objectMapper;


    @Autowired
    public SocketHandler(ObjectMapper objectMapper){

        this.objectMapper = objectMapper;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws InterruptedException, IOException {

        session.sendMessage(new TextMessage("Response from websocket"));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {

        String dashboardId = session.getUri().getQuery();

        if(!StringUtils.isEmpty(dashboardId)) {
            addToSessionsMap(session, dashboardId);
            LOGGER.info("Websocket session for {} added!", dashboardId);
        } else {
            session.close(new CloseStatus(1003, "No dashboardId provided"));
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String dashboardId = session.getUri().getQuery();
        removeFromSessionsMap(session, dashboardId);

        LOGGER.info("Websocket session for {} removed!", dashboardId);
    }

    public Set<String> getDashboardsWithSession(){

        return Collections.unmodifiableSet(sessionsPerDashboard.keySet());
    }

    public void sendMessageToDashboardSessions(Map<String, Object> stringMessage, String dashboardId) {

        List<WebSocketSession> sessions = sessionsPerDashboard.get(dashboardId);

        if(sessions != null){

            for(WebSocketSession webSocketSession : sessions) {

                try {
                    String jsonMessage = objectMapper.writeValueAsString(stringMessage);
                    webSocketSession.sendMessage(new TextMessage(jsonMessage));
                } catch (IOException e) {
                    LOGGER.error("Exception while sending message to session {}", webSocketSession.getId());
                }

            }
        }
    }

    private synchronized void addToSessionsMap(WebSocketSession session, String dashboardId) {

        List<WebSocketSession> dashboardSessions = sessionsPerDashboard.get(dashboardId);

        if(dashboardSessions == null){
            dashboardSessions = new ArrayList<>();
        }

        dashboardSessions.add(session);
        sessionsPerDashboard.put(dashboardId, dashboardSessions);
    }

    private synchronized void removeFromSessionsMap(WebSocketSession session, String dashboardId){

        if(!StringUtils.isEmpty(dashboardId)){
            List<WebSocketSession> dashBoardSessions = sessionsPerDashboard.get(dashboardId);

            if(dashBoardSessions != null){
                dashBoardSessions.remove(session);
            }

            if(dashBoardSessions.isEmpty()){
                sessionsPerDashboard.remove(dashboardId);
            }
        }
    }
}
