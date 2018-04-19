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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetricStats;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

public class HistoricUserMetricRepositoryImpl implements HistoricUserMetricRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public HistoricUserMetricRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<HistoricUserMetricStats> getUserMetricTendencyForPeriod(List<String> viewIds, ChronoUnit unit, long timestamp) {

        TypedAggregation<HistoricUserMetric> aggregation = newAggregation(HistoricUserMetric.class,
            match(Criteria.where("viewId").in(getPatternsForViewIds(viewIds))
                .and("historicType").is(unit)
                .and("timestamp").gte(timestamp)),
            project("identifier", "viewId", "appVersion", "platform", "name", "value", "sampleSize", "collectorId")
                .and(ConditionalOperators.ifNull("sampleSize").then(1l)).as("sampleSize")
                .and(ConditionalOperators.when(Criteria.where("sampleSize").gt(0)).thenValueOf("$sampleSize").otherwise(1l)).as("sampleSize"),
            group("identifier")
                .sum("sampleSize").as("sampleSize")
                .sum("value").as("value")
                .first("name").as("name")
                .first("viewId").as("viewId")
                .first("appVersion").as("appVersion")
                .first("platform").as("platform")
                .first("collectorId").as("collectorId"),
            project("identifier", "viewId", "appVersion", "platform", "name", "value", "sampleSize", "collectorId"));

        AggregationResults<HistoricUserMetricStats> groupResults
            = mongoTemplate.aggregate(aggregation, "historic_user_metrics", HistoricUserMetricStats.class);

        return groupResults.getMappedResults();
    }

    private List<Pattern> getPatternsForViewIds(List<String> viewIds) {
        return viewIds
            .stream()
            .map((String id) -> Pattern.compile("^" + id + ".*$"))
            .collect(Collectors.toList());
    }
}
