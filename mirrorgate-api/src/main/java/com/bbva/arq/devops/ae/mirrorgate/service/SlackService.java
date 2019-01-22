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
package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.SlackDTO;
import java.util.Map;

public interface SlackService {

    /**
     * Call Slack to get a token
     *
     * @param team Slack team requested
     * @param client_id Slack client id requested
     * @param client_secret Slack client secrets requested
     * @param code Slack client code
     * @return Slack token under a SlackDTO
     */
    SlackDTO getToken(String team, String client_id, String client_secret, String code);

    /**
     * Get WebSocket URL to connect to Slack
     *
     * @param team Slack team requested
     * @param token Previous token generated for this application
     * @return WebSocket URL to connect to Slack under a SlackDTO
     */
    SlackDTO getWebSocket(String team, String token);

    /**
     * Get channel list for team and token
     * @param slackToken Slack token
     * @return List of channels in the Slack team
     */
    Map<String,String> getChannelList(String slackToken);
}
