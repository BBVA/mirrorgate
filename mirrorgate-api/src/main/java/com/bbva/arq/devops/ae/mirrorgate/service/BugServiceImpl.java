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

import static com.bbva.arq.devops.ae.mirrorgate.core.utils.BugPriority.*;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.BugDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.BugPriority;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.BugStatus;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.IssueStatus;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.IssueType;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.repository.FeatureRepository;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author enrique
 */
@Service
public class BugServiceImpl implements BugService {

    @Autowired
    private FeatureRepository repository;

    @Override
    public List<BugDTO> getActiveBugsByBoards(List<String> boards) {

        List<Feature> issues = repository.findBySProjectNameInAndSTypeNameAndSStatusNot(boards, IssueType.BUG.getName(), IssueStatus.DONE.getName());

        return issues.stream()
                .map((issue) -> new BugDTO()
                        .setId(issue.getsNumber())
                        .setPriority(issuePriorityToBugPriority.get(issue.getPriority()))
                        .setStatus(BugStatus.fromName(issue.getsStatus()))
                )
                .collect(Collectors.toList());

    }

    private static final HashMap<String, BugPriority> issuePriorityToBugPriority;

    static {
        issuePriorityToBugPriority = new HashMap();
        issuePriorityToBugPriority.put("Highest", CRITICAL);
        issuePriorityToBugPriority.put("High", MAJOR);
        issuePriorityToBugPriority.put("Medium", MEDIUM);
        issuePriorityToBugPriority.put("Low", MINOR);
        issuePriorityToBugPriority.put("Lowest", MINOR);
    }

}
