package com.bbva.arq.devops.ae.mirrorgate.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);

    private Map<String, List<WebSocketSession>> sessionsPerDashboard = new HashMap<>(1000);


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws InterruptedException, IOException {

        session.sendMessage(new TextMessage("Response from websocket"));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        String dashboardId = session.getUri().getQuery();
        addToSessionsMap(session, dashboardId);

        LOGGER.info("Websocket session added!");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String dashboardId = session.getUri().getQuery();
        removeFromSessionsMap(session, dashboardId);

        LOGGER.info("Websocket session removed!");
    }

    public void broadcastMessage(String stringMessage, List<String> dashboardId) throws IOException {

        dashboardId.forEach(d -> sendMessageToDashboardSessions(stringMessage, d));
    }

    public Set getDashboardsWithSession(){

        return Collections.unmodifiableSet(sessionsPerDashboard.keySet());
    }

    private void sendMessageToDashboardSessions(String stringMessage, String dashboardId) {

        List<WebSocketSession> sessions = sessionsPerDashboard.get(dashboardId);

        if(sessions != null){

            for(WebSocketSession webSocketSession : sessions) {
                try {
                    webSocketSession.sendMessage(new TextMessage(stringMessage));
                } catch (IOException e) {
                    LOGGER.error("Exception while sending message to session {}", webSocketSession.getId());
                }
            }

        }

    }

    private synchronized void addToSessionsMap(WebSocketSession session, String dashboardId){

        if(!StringUtils.isEmpty(dashboardId)){
            List<WebSocketSession> dashboardSessions = sessionsPerDashboard.get(dashboardId);

            if(dashboardSessions == null){
                dashboardSessions = new ArrayList<>();
            }

            dashboardSessions.add(session);

            sessionsPerDashboard.put(dashboardId, dashboardSessions);
        }

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
