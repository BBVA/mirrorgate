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

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.SlackDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.SlackService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author enrique
 */
@RestController
public class NotificationController {

    private final DashboardService dashboardService;
    private final SlackService slackService;

    @Autowired
    public NotificationController(DashboardService dashboardService, SlackService slackService) {
        this.dashboardService = dashboardService;
        this.slackService = slackService;
    }

    @RequestMapping(value = "/backoffice/utils/slack-code-capturer",
            method = GET,
            produces = TEXT_HTML_VALUE)
    public String getSlackCode(@RequestParam("code") String code) {
        return "<html><head><script>opener.postMessage('"+code+"',document.location.origin);window.close();</script></head></html>";
    }

    @RequestMapping(value = "/backoffice/utils/slack-token-generator",
            method = GET,
            produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<?> getSlackToken(
            @RequestParam("code") String code,
            @RequestParam("clientId") String clientId,
            @RequestParam("team") String team,
            @RequestParam("clientSecret") String clientSecret
    ) {

        SlackDTO notification = slackService.getToken(team, clientId, clientSecret, code);

        if (!notification.isOk()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(notification.getError());
        }

        return ResponseEntity.ok(notification.getAccess_token());
    }

    @RequestMapping(value = "/dashboards/{name}/notifications",
            method = GET,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWebSocket(@PathVariable("name") String name) {
        Dashboard dashboard;

        try {
            dashboard = dashboardService.getDashboard(name);
        } catch (com.bbva.arq.devops.ae.mirrorgate.utils.MirrorGateException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
        }

        if (dashboard == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dashboard not found");
        }
        SlackDTO notification = slackService.getWebSocket(
                dashboard.getSlackTeam(),
                dashboard.getSlackToken());

        if (!notification.isOk()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(notification.getError());
        }

        return ResponseEntity.ok(notification.getUrl());
    }
}
