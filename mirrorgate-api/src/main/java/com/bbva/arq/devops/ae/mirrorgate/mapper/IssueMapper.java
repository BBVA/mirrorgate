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

package com.bbva.arq.devops.ae.mirrorgate.mapper;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.ProjectDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.SprintDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.IssuePriority;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.IssueStatus;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.SprintStatus;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;

/**
 * Created by alfonso on 28/05/17.
 */
public class IssueMapper {

    private IssueMapper(){}

    public static Feature map(IssueDTO source) {
        return map(source, new Feature());
    }

    public static Feature map(IssueDTO source, Feature target) {
        target.setdEstimate(source.getEstimate());
        target.setsName(source.getName());
        target.setsId(source.getId().toString());
        target.setsStatus(source.getStatus().getName());
        if(source.getPriority() != null) {
            target.setPriority(source.getPriority().getName());
        } else {
            target.setPriority(null);
        }
        
        target.setKeywords(source.getKeywords());
        target.setsTypeName(source.getType());
        target.setsNumber(source.getJiraKey());
        target.setlParentId(source.getParentId() == null ? null : Long.parseLong(source.getParentId()));
        target.setsParentKey(source.getParentKey());
        target.setsPiNames(source.getPiNames());
        target.setCollectorId(source.getCollectorId());
        SprintDTO sprint = source.getSprint();
        if(sprint != null) {
            target.setsSprintID(sprint.getId());
            target.setsSprintName(sprint.getName());
            target.setsSprintAssetState(sprint.getStatus() == null ? null : sprint.getStatus().name());
            target.setSprintBeginDate(sprint.getStartDate());
            target.setSprintEndDate(sprint.getEndDate());
        } else {
            target.setsSprintID(null);
            target.setsSprintName(null);
            target.setsSprintAssetState(null);
            target.setSprintBeginDate(null);
            target.setSprintEndDate(null);
        }
        ProjectDTO project = source.getProject();
        if(project != null) {
            target.setsProjectId(project.getId() == null ? null : project.getId().toString());
            target.setsProjectName(project.getName());
        } else {
            target.setsProjectId(null);
            target.setsProjectName(null);
        }
        return target;
    }

    public static IssueDTO map(Feature source) {
        return map(source, new IssueDTO());
    }

    public static IssueDTO map(Feature source, IssueDTO target) {
        target.setEstimate(source.getdEstimate());
        target.setName(source.getsName());
        target.setId(source.getsId() == null ? null : Long.parseLong(source.getsId()));
        target.setStatus(IssueStatus.fromName(source.getsStatus()));
        target.setType(source.getsTypeName());
        target.setParentId(source.getlParentId() == null ? null : source.getlParentId().toString());
        target.setSprint(new SprintDTO()
                .setId(source.getsSprintID())
                .setName(source.getsSprintName())
                .setStatus(source.getsSprintAssetState() == null ?
                        null :
                        SprintStatus.valueOf(source.getsSprintAssetState())
                )
                .setStartDate(source.getSprintBeginDate())
                .setEndDate(source.getSprintEndDate())
        );
        target.setProject(new ProjectDTO()
                .setId(source.getsProjectId() == null ? null : Long.parseLong(source.getsProjectId()))
                .setName(source.getsProjectName())
        );
        target.setParentKey(source.getsParentKey());
        target.setParentKey(source.getsParentKey());
        target.setJiraKey(source.getsNumber());
        target.setKeywords(source.getKeywords());
        target.setPiNames(source.getsPiNames());
        target.setCollectorId(source.getCollectorId());
        target.setPriority(source.getPriority() == null ? null : IssuePriority.fromName(source.getPriority()));

        return target;
    }


}
