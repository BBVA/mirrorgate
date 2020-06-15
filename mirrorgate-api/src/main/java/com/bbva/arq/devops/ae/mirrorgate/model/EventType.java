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

package com.bbva.arq.devops.ae.mirrorgate.model;

import java.util.HashMap;
import java.util.Map;

public enum EventType {
    DETAIL("DashboardType"),
    BUILD("BuildType"),
    ISSUE("IssueType"),
    REVIEW("ReviewType"),
    PING("PingType"),
    NOTIFICATION("NotificationType");

    private final String value;

    EventType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static final Map<String, EventType> MAPPING = new HashMap<>();

    static {
        MAPPING.put("IssueType", ISSUE);
        MAPPING.put("BuildType", BUILD);
        MAPPING.put("ReviewType", REVIEW);
        MAPPING.put("DashboardType", DETAIL);
        MAPPING.put("PingType", PING);
        MAPPING.put("NotificationType", NOTIFICATION);
    }

    public static EventType fromString(final String value) {
        if (MAPPING.containsKey(value)) {
            return MAPPING.get(value);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
