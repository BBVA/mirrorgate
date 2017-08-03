package com.bbva.arq.devops.ae.mirrorgate.websocket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws InterruptedException, IOException {

        session.sendMessage(new TextMessage("Response from websocket"));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        LOGGER.info("Websocket session added!");
    }

    public void broadcastMessage(String stringMessage) throws IOException {

        for(WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(new TextMessage(stringMessage));
        }
    }
}
