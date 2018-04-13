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
package com.bbva.arq.devops.ae.mirrorgate.support;

import java.util.HashMap;
import java.util.Map;

public enum IssueType {

    EPIC("Epic"),
    STORY("Story"),
    FEATURE("Feature"),
    BUG("Bug"),
    TASK("Task");

    private static final Map<String, IssueStatus> NAME_MAP = new HashMap<String, IssueStatus>() {
        {
            for (IssueStatus st : IssueStatus.values()) {
                put(st.getName(), st);
            }
        }
    };

    private final String name;

    IssueType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static IssueStatus fromName(String name) {
        return name == null ? null : NAME_MAP.get(name);
    }

}
