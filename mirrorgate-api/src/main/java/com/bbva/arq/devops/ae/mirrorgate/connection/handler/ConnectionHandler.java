/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.connection.handler;

import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ConnectionHandler {

    Set<String> getDashboardsWithSession();

    void addToSessionsMap(final SseEmitter session, final String dashboardId);

    void removeFromSessionsMap(final SseEmitter session, final String dashboardId);

    void sendEventUpdateMessage(final EventType event, final String dashboardId);

    void sendEventUpdateMessageToAll(final EventType event);
}
