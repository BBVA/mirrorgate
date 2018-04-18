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
package com.bbva.arq.devops.ae.mirrorgate.dto;

import com.bbva.arq.devops.ae.mirrorgate.support.BugPriority;
import com.bbva.arq.devops.ae.mirrorgate.support.BugStatus;

public class BugDTO {

    private String id;
    private BugPriority priority;
    private BugStatus status;

    public String getId() {
        return id;
    }

    public BugDTO setId(String id) {
        this.id = id;
        return this;
    }

    public BugPriority getPriority() {
        return priority;
    }

    public BugDTO setPriority(BugPriority priority) {
        this.priority = priority;
        return this;
    }

    public BugStatus getStatus() {
        return status;
    }

    public BugDTO setStatus(BugStatus status) {
        this.status = status;
        return this;
    }

}
