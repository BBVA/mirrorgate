/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.api;

import com.bbva.arq.devops.ae.mirrorgate.connection.handler.ConnectionHandler;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class ServerSentEventsController {

    private static final Logger LOG = LoggerFactory.getLogger(ServerSentEventsController.class);

    private final ConnectionHandler handler;

    private static class NotCachedSseEmitter extends SseEmitter {
        @Override
        protected void extendResponse(final ServerHttpResponse outputMessage) {
            outputMessage.getHeaders().add("X-Accel-Buffering", "no");
            outputMessage.getHeaders().add("Cache-Control", "no-cache;");

            super.extendResponse(outputMessage);
        }
    }


    @Autowired
    public ServerSentEventsController(final ConnectionHandler handler) {
        this.handler = handler;
    }

    @GetMapping(value = "/emitter/{dashboardId}")
    public SseEmitter serverSideEmitter(final @PathVariable String dashboardId) throws IOException {

        LOG.info("Creating SseEmitter for dashboard {}", dashboardId);

        final SseEmitter sseEmitter = new NotCachedSseEmitter();

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
