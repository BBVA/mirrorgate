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

import com.bbva.arq.devops.ae.mirrorgate.model.Collector;
import com.bbva.arq.devops.ae.mirrorgate.repository.CollectorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by alfonso on 28/05/17.
 */

@Component
public class CollectorServiceImpl implements CollectorService{

    @Autowired
    CollectorsRepository collectorsRepository;

    @Override
    public Date getLastExecutionDate(String id) {
        Collector c = collectorsRepository.findByCollectorId(id);
        return c == null ? null : c.getLastExecution();
    }

    @Override
    public void saveLastExecutionDate(String id, Date executionDate) {
        Collector c = collectorsRepository.findByCollectorId(id);

        if(c == null) {
            c = new Collector();
            c.setCollectorId(id);
        }
        c.setLastExecution(executionDate);

        collectorsRepository.save(c);

    }
}
