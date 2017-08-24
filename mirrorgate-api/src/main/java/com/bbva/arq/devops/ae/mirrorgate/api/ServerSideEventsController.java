package com.bbva.arq.devops.ae.mirrorgate.api;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ServerSideEventsHandler;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class ServerSideEventsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSideEventsController.class);

    private ServerSideEventsHandler handler;


    @Autowired
    public ServerSideEventsController(ServerSideEventsHandler handler){

        this.handler = handler;
    }

    @GetMapping(value = "/emitter/{dashboardId}")
    public SseEmitter serverSideEmitter(@PathVariable String dashboardId) throws IOException {

        LOGGER.info("Creating SseEmitter for dashboard {}", dashboardId);

        SseEmitter sseEmitter = new SseEmitter();

        sseEmitter.onCompletion(() -> {
            handler.removeFromSessionsMap(sseEmitter, dashboardId);
            sseEmitter.complete();
        });

        sseEmitter.onTimeout(() -> {
            handler.removeFromSessionsMap(sseEmitter, dashboardId);
            sseEmitter.complete();
        });

        handler.addToSessionsMap(sseEmitter, dashboardId);

        sseEmitter.send(SseEmitter.event().reconnectTime(0L));

        return sseEmitter;

    }
}
