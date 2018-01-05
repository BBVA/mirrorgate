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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.CommitDTO;
import com.bbva.arq.devops.ae.mirrorgate.mapper.CommitMapper;
import com.bbva.arq.devops.ae.mirrorgate.model.Commit;
import com.bbva.arq.devops.ae.mirrorgate.repository.CommitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CommitServiceImpl implements CommitService{

    private static final Logger LOGGER = LoggerFactory.getLogger(CommitServiceImpl.class);

    private final CommitRepository repository;

    @Autowired
    public CommitServiceImpl(CommitRepository repository){
        this.repository = repository;
    }

    @Override
    public List<CommitDTO> saveCommits(Iterable<CommitDTO> commits) {
        LOGGER.info("Saving commits");

        List<Commit> toSave = StreamSupport.stream(commits.spliterator(), false)
            .map(CommitMapper::map)
            .collect(Collectors.toList());

        Iterable<Commit> saved = repository.save(toSave);

        return StreamSupport.stream(saved.spliterator(), false)
            .map(CommitMapper::map)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getListOfCommits(String repo) {
        return repository.findAllByRepositoryOrderByTimestampDesc(repo)
            .stream()
            .map(c -> c.getHash())
            .collect(Collectors.toList());
    }

}
