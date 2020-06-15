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

package com.bbva.arq.devops.ae.mirrorgate.model;

import java.util.List;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification")
@CompoundIndex (name = "dashboard_and_timestamp", def = "{ 'dashboardsToNotify': 1, 'timestamp': 1 }")
public class Notification extends BaseIdModel {

    private String message;
    private List<String> dashboardsToNotify;
    private long timestamp;

    public String getMessage() {
        return message;
    }

    public Notification setMessage(final String message) {
        this.message = message;
        return this;
    }

    public List<String> getDashboardsToNotify() {
        return dashboardsToNotify;
    }

    public Notification setDashboardsToNotify(final List<String> dashboardsToNotify) {
        this.dashboardsToNotify = dashboardsToNotify;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Notification setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
