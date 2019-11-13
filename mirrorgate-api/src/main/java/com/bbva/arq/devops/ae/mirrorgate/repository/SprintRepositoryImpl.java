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

import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import com.bbva.arq.devops.ae.mirrorgate.model.Sprint;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class SprintRepositoryImpl implements SprintRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final Map<String, String> ISSUE_FIELDS = new HashMap<String, String>() {{
        for (Field f : Issue.class.getDeclaredFields()) {
            put(f.getName(), "$" + f.getName());
        }
    }};

    private static final Map<String,String> SPRINT_FIELDS = new HashMap<String,String>(){{
        put("sprintId", "sprintId");
        put("sprintName", "name");
        put("sprintAssetState", "status");
        put("sprintBeginDate","startDate");
        put("sprintEndDate","endDate");
    }};

    private static GroupOperation firstIssueFields(GroupOperation operation) {
        return ISSUE_FIELDS.keySet().stream()
                .reduce(operation, (o,s) -> o.first(s).as(s), (old,o) -> o);
    }

    private static GroupOperation firstSprintFields(GroupOperation operation) {
        return SPRINT_FIELDS.keySet().stream()
                .reduce(operation, (o,s) -> o.first(s).as(SPRINT_FIELDS.get(s)), (old,o) -> o);
    }

    @Override
    public List<Sprint> getSprintSampleForStatus(String[] status, String collectorId) {
        Aggregation agg = newAggregation(
            match(where("sprintAssetState").in(Arrays.asList(status)).and("collectorId").is(collectorId)),
            firstIssueFields(group("sprintId", "sprintAssetState")),
            firstSprintFields(group("sprintId", "sprintAssetState"))
                .push(new BasicDBObject(ISSUE_FIELDS)).as("issues")
        );
        AggregationResults<Sprint> aggregate =
            mongoTemplate.aggregate(agg, "issue", Sprint.class);

        return aggregate.getMappedResults();
    }

    @Override
    public Sprint getSprintForId(String id, String collectorId) {
        Aggregation agg = newAggregation(
            match(where("sprintId").is(id).and("collectorId").is(collectorId)),
            firstSprintFields(group("sprintId"))
                .push(new BasicDBObject(ISSUE_FIELDS)).as("issues")
        );

        AggregationResults<Sprint> aggregate =
            mongoTemplate.aggregate(agg, "issue", Sprint.class);

        return aggregate.getUniqueMappedResult();
    }

}
