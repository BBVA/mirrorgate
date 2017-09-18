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

import static com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus.DELETED;
import static com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus.TRANSIENT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.bbva.arq.devops.ae.mirrorgate.dto.ImageStreamDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.util.DBObjectUtils;

public class DashboardRepositoryImpl implements DashboardRepositoryCustom {

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

        return StreamSupport.stream(DASHBOARD_FIELDS.keySet().spliterator(),false)
                .reduce(operation, (o,s) -> o.first(s).as(s), (old,o) -> o);

    }

    @Override
    public List<Dashboard> getActiveDashboards() {

        Aggregation aggregation = newAggregation(
                sort(new Sort(Sort.Direction.DESC, "lastModification")),
                firstDashboardFields(group("name")),
                match(Criteria.where("status").nin(DELETED, TRANSIENT)),
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
    public ImageStreamDTO readFile(String name, String expectedEtag) {
        List<GridFSDBFile> files = gridFsTemplate.find(
                new Query().addCriteria(Criteria.where("filename").is(name))
        );

        ImageStreamDTO is = null;

        if(files.size() > 0) {
            is = new ImageStreamDTO();
            GridFSDBFile file = files.get(files.size() - 1);
            String etag = file.getMD5();
            is.setEtag(etag);
            if(etag == null || !etag.equals(expectedEtag)) {
                is.setImageStream(file.getInputStream());
            }
        }

        return is;
    }

}
