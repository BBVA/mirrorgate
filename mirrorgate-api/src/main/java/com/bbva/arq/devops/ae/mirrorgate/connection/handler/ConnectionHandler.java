package com.bbva.arq.devops.ae.mirrorgate.connection.handler;

import java.util.Map;
import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ConnectionHandler {

    Set<String> getDashboardsWithSession();

    void sendMessageToDashboardSessions(Map<String, Object> stringMessage, String dashboardId);

    void addToSessionsMap(SseEmitter session, String dashboardId);

    void removeFromSessionsMap(SseEmitter session, String dashboardId);

}
