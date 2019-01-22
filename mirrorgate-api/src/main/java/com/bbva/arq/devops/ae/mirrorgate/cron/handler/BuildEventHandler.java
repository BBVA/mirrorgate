package com.bbva.arq.devops.ae.mirrorgate.cron.handler;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "BuildType")
public class BuildEventHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildEventHandler.class);

    private final ConnectionHandler connectionHandler;

    @Autowired
    private BuildEventHandler(ConnectionHandler connectionHandler){

        this.connectionHandler = connectionHandler;
    }

    @Override
    public void processEvents(List<Event> eventList, Set<String> dashboardIds) {

        sendBuildEventToDashboards(dashboardIds);
    }

    private void sendBuildEventToDashboards(Set<String> dashboardIds) {
        dashboardIds.forEach(dashboardId -> {
            try {
                connectionHandler.sendEventUpdateMessage(EventType.BUILD, dashboardId);
            } catch (Exception unhandledException) {
                LOGGER.error("Unhandled exception calculating response of event", unhandledException);
            }
        });
    }
}
