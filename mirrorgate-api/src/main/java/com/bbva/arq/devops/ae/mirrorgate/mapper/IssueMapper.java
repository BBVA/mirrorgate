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
import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.support.IssuePriority;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueStatus;
import com.bbva.arq.devops.ae.mirrorgate.support.SprintStatus;
import java.util.Date;
import java.util.stream.Collectors;

public class IssueMapper {

    private IssueMapper() {
    }

    public static Issue map(final IssueDTO source) {
        return map(source, new Issue());
    }

    public static Issue map(final IssueDTO source, final Issue target) {
        target
            .setEstimation(source.getEstimate())
            .setName(source.getName())
            .setIssueId(source.getId().toString())
            .setStatus(source.getStatus().getName())
            .setPriority(source.getPriority() == null ? null : source.getPriority().getName())
            .setKeywords(source.getKeywords())
            .setType(source.getType())
            .setNumber(source.getJiraKey())
            .setParentsIds(
                source.getParentId() == null ? null : source.getParentId()
                    .stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList())
            )
            .setParentsKeys(source.getParentKey())
            .setPiNames(source.getPiNames())
            .setCollectorId(source.getCollectorId())
            .setUrl(source.getUrl())
            .setTimestamp(source.getUpdatedDate() == null ? null : source.getUpdatedDate().getTime())
            .setTeamName(source.getTeamName());

        final SprintDTO sprint = source.getSprint();
        if (sprint != null) {
            target.setSprintId(sprint.getId())
                .setSprintName(sprint.getName())
                .setSprintAssetState(sprint.getStatus() == null ? null : sprint.getStatus().name())
                .setSprintBeginDate(sprint.getStartDate())
                .setSprintEndDate(sprint.getEndDate());
        } else {
            target.setSprintId(null)
                .setSprintName(null)
                .setSprintAssetState(null)
                .setSprintBeginDate(null)
                .setSprintEndDate(null);
        }

        final ProjectDTO project = source.getProject();
        if (project != null) {
            target.setProjectId(project.getId() == null ? null : project.getId().toString())
                .setProjectName(project.getName());
        } else {
            target.setProjectId(null)
                .setProjectName(null);
        }

        return target;
    }

    public static IssueDTO map(final Issue source) {
        return map(source, new IssueDTO());
    }

    private static IssueDTO map(final Issue source, final IssueDTO target) {
        return target
            .setEstimate(source.getEstimation())
            .setName(source.getName())
            .setId(source.getIssueId() == null ? null : Long.parseLong(source.getIssueId()))
            .setStatus(IssueStatus.fromName(source.getStatus()))
            .setType(source.getType())
            .setSprint(new SprintDTO()
                .setId(source.getSprintId())
                .setName(source.getSprintName())
                .setStatus(
                    source.getSprintAssetState() == null ? null : SprintStatus.valueOf(source.getSprintAssetState())
                )
                .setStartDate(source.getSprintBeginDate())
                .setEndDate(source.getSprintEndDate()))
            .setProject(new ProjectDTO()
                .setId(source.getProjectId() == null ? null : Long.parseLong(source.getProjectId()))
                .setName(source.getProjectName()))
            .setParentKey(source.getParentsKeys())
            .setParentId(
                source.getParentsIds() == null ? null : source.getParentsIds()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList())
            )
            .setJiraKey(source.getNumber())
            .setKeywords(source.getKeywords())
            .setPiNames(source.getPiNames())
            .setCollectorId(source.getCollectorId())
            .setPriority(source.getPriority() == null ? null : IssuePriority.fromName(source.getPriority()))
            .setUrl(source.getUrl())
            .setUpdatedDate(source.getTimestamp() == null ? null : new Date(source.getTimestamp()))
            .setTeamName(source.getTeamName());
    }
}
