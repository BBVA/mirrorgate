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

package com.bbva.arq.devops.ae.mirrorgate.core.utils;

import java.util.Map;

public class Filters {

    private int timeSpan;
    private Map<String,Boolean> branch;
    private Map<String,Boolean> status;

    public int getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(int timeSpan) {
        this.timeSpan = timeSpan;
    }

    public Map<String, Boolean> getBranch() {
        return branch;
    }

    public void setBranch(Map<String, Boolean> branch) {
        this.branch = branch;
    }

    public Map<String, Boolean> getStatus() {
        return status;
    }

    public void setStatus(Map<String, Boolean> status) {
        this.status = status;
    }
}
