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
package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.BugDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.repository.IssueRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.BugPriority;
import com.bbva.arq.devops.ae.mirrorgate.support.BugStatus;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueStatus;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BugServiceImpl implements BugService {

    @Autowired
    private IssueRepository repository;

    @Override
    public List<BugDTO> getActiveBugsByBoards(List<String> boards) {

        final List<Issue> issues = repository.findByKeywordsInAndTypeAndStatusNot(boards, IssueType.BUG.getName(), IssueStatus.DONE.getName());

        return issues.stream()
            .map((issue) -> new BugDTO()
                .setId(issue.getNumber())
                .setPriority(PRIORITY_MAP.get(issue.getPriority()))
                .setStatus(BugStatus.fromName(issue.getStatus()))
            ).collect(Collectors.toList());
    }

    private static final Map<String, BugPriority> PRIORITY_MAP = new HashMap<>() {{
        put("Highest", BugPriority.CRITICAL);
        put("High", BugPriority.MAJOR);
        put("Medium", BugPriority.MEDIUM);
        put("Low", BugPriority.MINOR);
        put("Lowest", BugPriority.MINOR);
    }};

}
