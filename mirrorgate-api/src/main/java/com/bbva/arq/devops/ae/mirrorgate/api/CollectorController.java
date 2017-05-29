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

package com.bbva.arq.devops.ae.mirrorgate.api;

import com.bbva.arq.devops.ae.mirrorgate.service.CollectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


/**
 * Defines feature rest methods
 */
@RestController
public class CollectorController {

    private final CollectorService collectorService;

    @Autowired
    public CollectorController(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @RequestMapping(value = "/api/collectors/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Date> getLastExecutionDate(@PathVariable("id") String id) {
         Date date = collectorService.getLastExecutionDate(id);

         return date == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(date);

    }

    @RequestMapping(value = "/api/collectors/{id}", method = PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity setLastExecutionDate(@PathVariable("id") String id, @RequestBody Date executionDate) {
        collectorService.saveLastExecutionDate(id, executionDate);
        return ResponseEntity.ok().build();
    }

}
