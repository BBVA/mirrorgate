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

import com.bbva.arq.devops.ae.mirrorgate.support.SprintStatus;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SprintDTO implements Serializable {

    private String id;
    private String name;
    private SprintStatus status;
    private Date startDate;
    private Date endDate;
    private Date completeDate;
    private List<IssueDTO> issues;

    public String getId() {
        return id;
    }

    public SprintDTO setId(final String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SprintDTO setName(final String name) {
        this.name = name;
        return this;
    }

    public SprintStatus getStatus() {
        return status;
    }

    public SprintDTO setStatus(final SprintStatus status) {
        this.status = status;
        return this;
    }

    public Date getStartDate() {
        return startDate == null ? null : new Date(startDate.getTime());
    }

    public SprintDTO setStartDate(final Date startDate) {
        this.startDate = startDate == null ? null : new Date(startDate.getTime());
        return this;
    }

    public Date getEndDate() {
        return endDate == null ? null : new Date(endDate.getTime());
    }

    public SprintDTO setEndDate(final Date endDate) {
        this.endDate = endDate == null ? null : new Date(endDate.getTime());
        return this;
    }

    public Date getCompleteDate() {
        return completeDate == null ? null : new Date(completeDate.getTime());
    }

    public SprintDTO setCompleteDate(final Date completeDate) {
        this.completeDate = completeDate == null ? null : new Date(completeDate.getTime());
        return this;
    }

    public List<IssueDTO> getIssues() {
        return issues;
    }

    public SprintDTO setIssues(final List<IssueDTO> issues) {
        this.issues = issues;
        return this;
    }

}
