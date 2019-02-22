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

package com.bbva.arq.devops.ae.mirrorgate.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "collectors")
public class Collector implements BaseModel {

    @Id
    private String id;
    private String collectorId;
    private Date lastExecution;

    public String getCollectorId() {
        return collectorId;
    }

    public Collector setCollectorId(String collectorId) {
        this.collectorId = collectorId;
        this.id = collectorId;
        return this;
    }

    public Date getLastExecution() {
        return lastExecution == null ? null : new Date(lastExecution.getTime());
    }

    public Collector setLastExecution(Date lastExecution) {
        this.lastExecution = new Date(lastExecution.getTime());
        return this;
    }

    @Override
    public Object getId() {
        return this.id;
    }
}
