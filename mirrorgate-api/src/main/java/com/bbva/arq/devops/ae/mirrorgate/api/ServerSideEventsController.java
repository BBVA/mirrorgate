package com.bbva.arq.devops.ae.mirrorgate.api;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ServerSideEventsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class ServerSideEventsController {

    private ServerSideEventsHandler handler;


    @Autowired
    public ServerSideEventsController(ServerSideEventsHandler handler){

        this.handler = handler;
    }

    @GetMapping(value = "/emitter/{dashboardId}")
    public SseEmitter serverSideEmitter(@PathVariable String dashboardId){

        SseEmitter sseEmitter = new SseEmitter();

        sseEmitter.onCompletion( () -> {
            handler.removeFromSessionsMap(sseEmitter, dashboardId);
            sseEmitter.complete();
        });

        sseEmitter.onTimeout(() -> {
            handler.removeFromSessionsMap(sseEmitter, dashboardId);
            sseEmitter.complete();
        });

        handler.addToSessionsMap(sseEmitter, dashboardId);

        return sseEmitter;

    }
}
