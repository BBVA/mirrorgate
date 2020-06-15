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

import java.util.Date;
import java.util.List;

public class ProgramIncrementDTO {

    private String programIncrementName;
    private List<IssueDTO> programIncrementEpics;
    private List<IssueDTO> programIncrementFeatures;
    private List<IssueDTO> programIncrementStories;
    private Date startDate;
    private Date endDate;

    public String getProgramIncrementName() {
        return programIncrementName;
    }

    public ProgramIncrementDTO setProgramIncrementName(final String programIncrementName) {
        this.programIncrementName = programIncrementName;
        return this;
    }

    public List<IssueDTO> getProgramIncrementFeatures() {
        return programIncrementFeatures;
    }

    public ProgramIncrementDTO setProgramIncrementFeatures(final List<IssueDTO> programIncrementFeatures) {
        this.programIncrementFeatures = programIncrementFeatures;
        return this;
    }

    public List<IssueDTO> getProgramIncrementStories() {
        return programIncrementStories;
    }

    public ProgramIncrementDTO setProgramIncrementStories(final List<IssueDTO> programIncrementStories) {
        this.programIncrementStories = programIncrementStories;
        return this;
    }

    public List<IssueDTO> getProgramIncrementEpics() {
        return programIncrementEpics;
    }

    public ProgramIncrementDTO setProgramIncrementEpics(final List<IssueDTO> programIncrementEpics) {
        this.programIncrementEpics = programIncrementEpics;
        return this;
    }

    public Date getProgramIncrementStartDate() {
        return startDate == null ? null : new Date(startDate.getTime());
    }

    public ProgramIncrementDTO setProgramIncrementStartDate(final Date startDate) {
        this.startDate = startDate == null ? null : new Date(startDate.getTime());
        return this;
    }

    public Date getProgramIncrementEndDate() {
        return endDate == null ? null : new Date(endDate.getTime());
    }

    public ProgramIncrementDTO setProgramIncrementEndDate(final Date endDate) {
        this.endDate = endDate == null ? null : new Date(endDate.getTime());
        return this;
    }
}