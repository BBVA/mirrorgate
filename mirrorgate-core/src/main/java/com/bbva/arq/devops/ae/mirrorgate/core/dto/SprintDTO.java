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

package com.bbva.arq.devops.ae.mirrorgate.core.dto;

import com.bbva.arq.devops.ae.mirrorgate.core.utils.SprintStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by alfonso on 26/05/17.
 */
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

    public SprintDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SprintDTO setName(String name) {
        this.name = name;
        return this;
    }

    public SprintStatus getStatus() {
        return status;
    }

    public SprintDTO setStatus(SprintStatus status) {
        this.status = status;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public SprintDTO setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public SprintDTO setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public SprintDTO setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
        return this;
    }
    
    public List<IssueDTO> getIssues() {
        return issues;
    }

    public SprintDTO setIssues(List<IssueDTO> issues) {
        this.issues = issues;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof SprintDTO)) {
            return false;
        }
        SprintDTO s = (SprintDTO) o;
        return ((s.getId() == null && getId() == null) || s.getId().equals(getId())) &&
                ((s.getName() == null && getName() == null) || s.getName().equals(getName())) &&
                ((s.getStatus() == null && getStatus() == null) || s.getStatus().equals(getStatus())) &&
                ((s.getStartDate() == null && getStartDate() == null) || s.getStartDate().equals(getStartDate())) &&
                ((s.getEndDate() == null && getEndDate() == null) || s.getEndDate().equals(getEndDate())) &&
                true;
    }
}
