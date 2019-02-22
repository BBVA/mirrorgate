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

import java.util.Date;
import java.util.List;

public class Sprint {

    private String sid;
    private String name;
    private String status;
    private Date startDate;
    private Date endDate;
    private Date completeDate;
    private List<Feature> features;

    public String getId() {
        return sid;
    }

    public Sprint setId(String id) {
        this.sid = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Sprint setName(String name) {
        this.name = name;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Sprint setStatus(String status) {
        this.status = status;
        return this;
    }

    public Date getStartDate() {
        return startDate == null ? null : new Date(startDate.getTime());
    }

    public Sprint setStartDate(Date startDate) {
        this.startDate = startDate == null ? null : new Date(startDate.getTime());
        return this;
    }

    public Date getEndDate() {
        return endDate == null ? null : new Date(endDate.getTime());
    }

    public Sprint setEndDate(Date endDate) {
        this.endDate = endDate == null ? null : new Date(endDate.getTime());
        return this;
    }

    public Date getCompleteDate() {
        return completeDate == null ? null : new Date(completeDate.getTime());
    }

    public Sprint setCompleteDate(Date completeDate) {
        this.completeDate = completeDate == null ? null : new Date(completeDate.getTime());
        return this;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public Sprint setFeatures(List<Feature> features) {
        this.features = features;
        return this;
    }
}
