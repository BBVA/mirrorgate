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

/**
 * Enumeration of applications platforms.
 */
public enum Platform {
    Android("Android"),
    IOS("IOS"),
    Windows("Windows"),
    WindowsPhone("Windows Phone"),
    Unknown("Unknown"),
    All("All");

    private final String name;

    Platform(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private static final Map<String, Platform> MAPPING = new HashMap<>();

    static {
        for (final Platform platform : values()) {
            MAPPING.put(platform.getName().toLowerCase(), platform);
        }
    }

    public static Platform fromString(final String value) {
        final String key = (value != null) ? value.toLowerCase() : "All";
        return MAPPING.getOrDefault(key, Unknown);
    }

    public static String toString(final Platform platform) {
        return platform == null ? null : platform.getName();
    }
}
