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
package com.bbva.arq.devops.ae.mirrorgate.repository;

import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus.DELETED;
import static com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus.TRANSIENT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

public class DashboardRepositoryImpl implements DashboardRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    private static final Map<String, String> DASHBOARD_FIELDS = new HashMap<>() {{
        for (Field f : Dashboard.class.getDeclaredFields()) {
            put(f.getName(), "$" + f.getName());
        }
        remove("id");
    }};

    private static GroupOperation firstDashboardFields(GroupOperation operation) {

        return DASHBOARD_FIELDS.keySet().stream()
            .reduce(operation, (o, s) -> o.first(s).as(s), (old, o) -> o);

    }

    @Override
    public List<Dashboard> getActiveAndTransientDashboards() {
        return getDashboardsNotInStatus(DELETED);
    }


    @Override
    public List<Dashboard> getActiveDashboards() {
        return getDashboardsNotInStatus(DELETED, TRANSIENT);
    }

    private List<Dashboard> getDashboardsNotInStatus(DashboardStatus... status) {
        Aggregation aggregation = newAggregation(
            sort(Sort.by(Sort.Direction.DESC, "lastModification")),
            firstDashboardFields(group("name")),
            match(Criteria.where("status").nin(Collections.singletonList(status))),
            project(DASHBOARD_FIELDS.keySet().toArray(new String[]{})).andExclude("_id")
        );

        //Convert the aggregation result into a List
        AggregationResults<Dashboard> groupResults
            = mongoTemplate.aggregate(aggregation, Dashboard.class, Dashboard.class);
        return groupResults.getMappedResults();
    }

    @Override
    public void saveFile(InputStream image, String name) {
        gridFsTemplate.delete(new Query(GridFsCriteria.whereFilename().is(name)));
        gridFsTemplate.store(image, name);
    }

    @Override
    public InputStreamResource readFile(String name) {
        return gridFsTemplate.getResource(Objects.requireNonNull(name));
    }

}
