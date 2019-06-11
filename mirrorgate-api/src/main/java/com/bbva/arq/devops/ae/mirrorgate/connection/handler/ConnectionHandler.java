package com.bbva.arq.devops.ae.mirrorgate.connection.handler;

import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;

public interface ConnectionHandler {

    Set<String> getDashboardsWithSession();

    void addToSessionsMap(SseEmitter session, String dashboardId);

    void removeFromSessionsMap(SseEmitter session, String dashboardId);

    void sendEventUpdateMessage(EventType event, String dashboardId);

    void sendEventUpdateMessageToAll(EventType event);
}
