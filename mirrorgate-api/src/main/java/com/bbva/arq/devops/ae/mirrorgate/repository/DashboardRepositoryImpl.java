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
import com.bbva.arq.devops.ae.mirrorgate.model.ImageStream;
import com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus.DELETED;
import static com.bbva.arq.devops.ae.mirrorgate.support.DashboardStatus.TRANSIENT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class DashboardRepositoryImpl implements DashboardRepositoryCustom {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    private static final Map<String,String> DASHBOARD_FIELDS = new HashMap<String, String>() {{
        for(Field f : Dashboard.class.getDeclaredFields()) {
            put(f.getName(), "$" + f.getName());
        }
        remove("id");
    }};

    private static GroupOperation firstDashboardFields(GroupOperation operation) {

        return DASHBOARD_FIELDS.keySet().stream()
                .reduce(operation, (o,s) -> o.first(s).as(s), (old,o) -> o);

    }

    @Override
    public List<Dashboard> getActiveAndTransientDashboards() {
        return getDashboardsNotInStatus(new DashboardStatus[]{DELETED});
    }


    @Override
    public List<Dashboard> getActiveDashboards() {
        return getDashboardsNotInStatus(new DashboardStatus[]{DELETED, TRANSIENT});
    }

    private List<Dashboard> getDashboardsNotInStatus(DashboardStatus[]  status) {
        Aggregation aggregation = newAggregation(
            sort(new Sort(Sort.Direction.DESC, "lastModification")),
            firstDashboardFields(group("name")),
            match(Criteria.where("status").nin((Object[])status)),
            project(DASHBOARD_FIELDS.keySet().toArray(new String[]{})).andExclude("_id")
        );

        //Convert the aggregation result into a List
        AggregationResults<Dashboard> groupResults
            = mongoTemplate.aggregate(aggregation, Dashboard.class, Dashboard.class);
        return groupResults.getMappedResults();
    }

    @Override
    public void saveFile(InputStream image, String name) {
        gridFsTemplate.store(image, name);
    }

    @Override
    public ImageStream readFile(String name) {
        GridFSFile file = gridFsTemplate.find(new Query().addCriteria(Criteria.where("filename").is(name))).first();

        if(file != null) {
            try {
                return new ImageStream()
                        .setImageStream(gridFsTemplate.getResource(file).getInputStream());
            } catch (IOException e) {
                LOGGER.error("There was an error trying to read a image form DB " + name, e);
            }
        }

        return null;
    }

}
