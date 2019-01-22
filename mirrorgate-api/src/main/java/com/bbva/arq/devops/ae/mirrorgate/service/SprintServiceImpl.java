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

package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.SprintDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.SprintMapper;
import com.bbva.arq.devops.ae.mirrorgate.repository.SprintRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.SprintStatus;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SprintServiceImpl implements SprintService {

    @Autowired
    SprintRepository sprintRepository;

    @Override
    public List<SprintDTO> getSampleForStatus(SprintStatus[] sprintStatuses, String collectorId) {
        return sprintRepository.getSprintSampleForStatus(Arrays.stream(sprintStatuses)
                .map(Object::toString)
                .collect(Collectors.toList())
                .toArray(new String[]{}),
                collectorId
        ).stream()
                .map(SprintMapper::map)
                .peek((s) -> {
                    //We are already returning the sprint... don't loop
                    if(s.getIssues() != null) {
                        s.getIssues().forEach((i) -> i.setSprint(null));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public SprintDTO getSprint(Long id, String collectorId) {
        com.bbva.arq.devops.ae.mirrorgate.model.Sprint sourceSprint = sprintRepository.getSprintForId(id.toString(), collectorId);
        SprintDTO sprint = SprintMapper.map(sourceSprint);

        //We are already returning the sprint... don't loop
        if(sprint != null) {
            sprint.getIssues().forEach((i) -> i.setSprint(null));
        }
        return sprint;
    }
}
