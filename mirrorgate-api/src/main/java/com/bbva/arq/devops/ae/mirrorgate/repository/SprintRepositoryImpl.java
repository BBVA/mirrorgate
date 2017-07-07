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

package com.bbva.arq.devops.ae.mirrorgate.repository;

import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.model.Sprint;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.StreamSupport;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by alfonso on 13/03/17.
 */
@Component
public class SprintRepositoryImpl implements SprintRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Map<String,String> FEATURE_FIELDS = new HashMap<String, String>() {{
        for(Field f : Feature.class.getDeclaredFields()) {
            put(f.getName(), "$" + f.getName());
        }
    }};

    private static final Map<String,String> SPRINT_FIELDS = new HashMap<String,String>(){{
        put("sSprintID","sid");
        put("sSprintName","name");
        put("sSprintAssetState","status");
        put("sprintBeginDate","startDate");
        put("sprintEndDate","endDate");
    }};

    private static GroupOperation firstFeatureFields(GroupOperation operation) {

        return StreamSupport.stream(FEATURE_FIELDS.keySet().spliterator(),false)
                .reduce(operation, (o,s) -> o.first(s).as(s), (old,o) -> o);

    }

    private static GroupOperation firstSprintFields(GroupOperation operation) {

        return StreamSupport.stream(SPRINT_FIELDS.keySet().spliterator(),false)
                .reduce(operation, (o,s) -> o.first(s).as(SPRINT_FIELDS.get(s)), (old,o) -> o);

    }

    @Override
    public List<Sprint> getSprintSampleForStatus(String[] status, String collectorId) {
        Aggregation agg = newAggregation(
                match(where("sSprintAssetState").in((Object[]) status).and("collectorId").is(collectorId)),
                firstFeatureFields(group("sSprintID","sSprintAssetState")),
                firstSprintFields(group("sSprintID","sSprintAssetState"))
                        .push(new BasicDBObject(FEATURE_FIELDS)).as("features")
        );
        AggregationResults<Sprint> aggregate =
                mongoTemplate.aggregate(agg, "feature", Sprint.class);

        return aggregate.getMappedResults();
    }

    @Override
    public Sprint getSprintForId(String id, String collectorId) {
        Aggregation agg = newAggregation(
                match(where("sSprintID").is(id).and("collectorId").is(collectorId)),
                firstSprintFields(group("sSprintID"))
                        .push(new BasicDBObject(FEATURE_FIELDS)).as("features")
        );

        AggregationResults<Sprint> aggregate =
                mongoTemplate.aggregate(agg, "feature", Sprint.class);

        return aggregate.getUniqueMappedResult();
    }

}
