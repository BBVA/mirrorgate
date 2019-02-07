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

import com.bbva.arq.devops.ae.mirrorgate.dto.IssueDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.ProjectDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.SprintDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.support.IssuePriority;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueStatus;
import com.bbva.arq.devops.ae.mirrorgate.support.SprintStatus;

import java.util.Date;
import java.util.stream.Collectors;

public class IssueMapper {

    private IssueMapper() {
    }

    public static Feature map(IssueDTO source) {
        return map(source, new Feature());
    }

    public static Feature map(IssueDTO source, Feature target) {
        target
            .setdEstimate(source.getEstimate())
            .setsName(source.getName())
            .setsId(source.getId().toString())
            .setsStatus(source.getStatus().getName())
            .setPriority(source.getPriority() == null ? null : source.getPriority().getName())
            .setKeywords(source.getKeywords())
            .setsTypeName(source.getType())
            .setsNumber(source.getJiraKey())
            .setlParentId(source.getParentId() == null ? null : source.getParentId().stream().map(Long::parseLong).collect(Collectors.toList()))
            .setsParentKey(source.getParentKey())
            .setsPiNames(source.getPiNames())
            .setCollectorId(source.getCollectorId())
            .setUrl(source.getUrl())
            .setTimestamp(source.getUpdatedDate() == null ? null : source.getUpdatedDate().getTime())
            .setTeamName(source.getTeamName());

        SprintDTO sprint = source.getSprint();
        if (sprint != null) {
            target.setsSprintID(sprint.getId())
                .setsSprintName(sprint.getName())
                .setsSprintAssetState(sprint.getStatus() == null ? null : sprint.getStatus().name())
                .setSprintBeginDate(sprint.getStartDate())
                .setSprintEndDate(sprint.getEndDate());
        } else {
            target.setsSprintID(null)
                .setsSprintName(null)
                .setsSprintAssetState(null)
                .setSprintBeginDate(null)
                .setSprintEndDate(null);
        }

        ProjectDTO project = source.getProject();
        if (project != null) {
            target.setsProjectId(project.getId() == null ? null : project.getId().toString())
                .setsProjectName(project.getName());
        } else {
            target.setsProjectId(null)
                .setsProjectName(null);
        }

        return target;
    }

    public static IssueDTO map(Feature source) {
        return map(source, new IssueDTO());
    }

    private static IssueDTO map(Feature source, IssueDTO target) {
        return target
            .setEstimate(source.getdEstimate())
            .setName(source.getsName())
            .setId(source.getsId() == null ? null : Long.parseLong(source.getsId()))
            .setStatus(IssueStatus.fromName(source.getsStatus()))
            .setType(source.getsTypeName())
            .setSprint(new SprintDTO()
                .setId(source.getsSprintID())
                .setName(source.getsSprintName())
                .setStatus(source.getsSprintAssetState() == null ? null : SprintStatus.valueOf(source.getsSprintAssetState()))
                .setStartDate(source.getSprintBeginDate())
                .setEndDate(source.getSprintEndDate()))
            .setProject(new ProjectDTO()
                .setId(source.getsProjectId() == null ? null : Long.parseLong(source.getsProjectId()))
                .setName(source.getsProjectName()))
            .setParentKey(source.getsParentKey())
            .setParentId(source.getlParentId() == null ? null : source.getlParentId().stream().map(String::valueOf).collect(Collectors.toList()))
            .setJiraKey(source.getsNumber())
            .setKeywords(source.getKeywords())
            .setPiNames(source.getsPiNames())
            .setCollectorId(source.getCollectorId())
            .setPriority(source.getPriority() == null ? null : IssuePriority.fromName(source.getPriority()))
            .setUrl(source.getUrl())
            .setUpdatedDate(source.getTimestamp() == null ? null : new Date(source.getTimestamp()))
            .setTeamName(source.getTeamName());
    }
}
