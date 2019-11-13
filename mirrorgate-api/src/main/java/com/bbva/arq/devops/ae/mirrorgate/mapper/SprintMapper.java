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

import com.bbva.arq.devops.ae.mirrorgate.dto.SprintDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Sprint;
import com.bbva.arq.devops.ae.mirrorgate.support.SprintStatus;

import java.util.stream.Collectors;

public class SprintMapper {

    private SprintMapper() {
    }

    public static SprintDTO map(Sprint source) {
        return source == null ? null : map(source, new SprintDTO());
    }

    private static SprintDTO map(Sprint source, SprintDTO target) {
        return target
            .setId(source.getSprintId())
            .setName(source.getName())
            .setStartDate(source.getStartDate())
            .setEndDate(source.getEndDate())
            .setStatus(source.getStatus() == null ? null : SprintStatus.valueOf(source.getStatus()))
            .setIssues(source.getIssues() == null ? null : source.getIssues().stream()
                .map(IssueMapper::map)
                .collect(Collectors.toList())
            );
    }

}
