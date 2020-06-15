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

package com.bbva.arq.devops.ae.mirrorgate.support;

import java.util.HashMap;
import java.util.Map;

public enum BugStatus {

    IN_PROGRESS("In Progress"),
    DONE("Done"),
    BACKLOG("Backlog"),
    WAITING("Waiting"),
    IMPEDED("Impeded");

    private static final Map<String, BugStatus> NAME_MAP = new HashMap<>() {
        {
            for (final BugStatus st : BugStatus.values()) {
                put(st.getName(), st);
            }
        }
    };

    private final String name;

    BugStatus(final String name) {
        this.name = name;
    }

    private String getName() {
        return name;
    }

    public static BugStatus fromName(final String name) {
        return name == null ? null : NAME_MAP.get(name);
    }

}
