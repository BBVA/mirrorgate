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
package com.bbva.arq.devops.ae.mirrorgate.api;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.CommitDTO;
import com.bbva.arq.devops.ae.mirrorgate.service.CommitService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


/**
 * Commit controller.
 */
@RestController
public class CommitController {

    private final CommitService commitService;
    private final DashboardService dashboardService;

    @Autowired
    public CommitController(CommitService commitService, DashboardService dashboardService) {
        this.commitService = commitService;
        this.dashboardService = dashboardService;
    }

    @RequestMapping(value = "/api/repositories", method = GET,
        produces = APPLICATION_JSON_VALUE)
    public List<String> getRepositories() {
        return dashboardService.getActiveDashboards()
            .stream()
            .filter(d -> d.getGitRepos() != null)
            .filter(d -> !d.getGitRepos().isEmpty())
            .flatMap(d -> d.getGitRepos().stream()
                .filter(r -> r != null)
                .filter(r -> !r.isEmpty()))
            .distinct()
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/commits", method = PUT,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCommits(@Valid @RequestBody Iterable<CommitDTO> request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commitService.saveCommits(request));
    }

    @RequestMapping(value = "/api/commits/lastcommit", method = GET,
        produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLastCommit(
        @RequestParam(value = "repo", required = true) String repo
        ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commitService.getLastCommit(repo));
    }

    @RequestMapping(value = "/api/commits/lastcommit", method = GET,
        produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTimeToMaster(
        @RequestParam(value = "repo", required = true) String repo,
        @RequestParam(value = "branch", required = true) String branch
        ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commitService.getTimeToMaster(repo, branch));
    }

}