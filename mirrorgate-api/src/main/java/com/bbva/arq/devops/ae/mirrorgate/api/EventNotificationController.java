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
package com.bbva.arq.devops.ae.mirrorgate.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.bbva.arq.devops.ae.mirrorgate.dto.EventNotificationDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.EventNotification;
import com.bbva.arq.devops.ae.mirrorgate.service.EventNotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventNotificationController {

    private final EventNotificationService eventNotificationService;

    @Autowired
    public EventNotificationController(EventNotificationService eventNotificationService){

        this.eventNotificationService = eventNotificationService;
    }


    @RequestMapping(method = GET, value = "/dashboards/{name}/event-notification", produces = APPLICATION_JSON_VALUE)
    public EventNotification getNotificationForDashboard(@PathVariable("name") String dashboardId){
        return eventNotificationService.getEventNotificationForDashboard(dashboardId);
    }

    @RequestMapping(method = POST, value = "/api/event-notification", produces = APPLICATION_JSON_VALUE)
    public EventNotification insertNotification(@RequestBody EventNotificationDTO eventNotificationDTO){
        return eventNotificationService.saveEventNotification(eventNotificationDTO);
    }
}
