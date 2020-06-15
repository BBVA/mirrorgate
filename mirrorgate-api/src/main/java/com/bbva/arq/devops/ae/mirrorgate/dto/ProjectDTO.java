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

package com.bbva.arq.devops.ae.mirrorgate.dto;

import java.io.Serializable;

public class ProjectDTO implements Serializable {

    private Long id;
    private String name;
    private String key;

    public Long getId() {
        return id;
    }

    public ProjectDTO setId(final Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProjectDTO setName(final String name) {
        this.name = name;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ProjectDTO setKey(final String key) {
        this.key = key;
        return this;
    }
}
