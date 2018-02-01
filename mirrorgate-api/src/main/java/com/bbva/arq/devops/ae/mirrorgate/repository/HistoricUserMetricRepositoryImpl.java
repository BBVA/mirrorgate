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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;


import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetricStats;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Divide;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Multiply;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

public class HistoricUserMetricRepositoryImpl implements HistoricUserMetricRepositoryCustom {

    private MongoTemplate mongoTemplate;

    @Autowired
    public HistoricUserMetricRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public List<HistoricUserMetricStats> getUserMetricAverageTendencyForPeriod(List<String> views, ChronoUnit unit, List<String> metricNames, long timestamp){

        Cond sampleSizeCondition = ConditionalOperators.when(Criteria.where("sampleSize").gt(0))
            .thenValueOf("$sampleSize").otherwise(1l);

        TypedAggregation<HistoricUserMetric> aggregation = newAggregation(HistoricUserMetric.class,
            match(Criteria.where("viewId").in(views)
                .and("name").in(metricNames)
                .and("value").gte(0d)
                .and("historicType").is(unit)
                .and("timestamp").gte(timestamp)),
            project("viewId", "name", "value", "historicType", "sampleSize", "collectorId", "appVersion", "identifier")
                .and(ConditionalOperators.ifNull("sampleSize").then(1l)).as("sampleSize"),
            project("viewId", "name", "value", "historicType", "sampleSize", "collectorId", "appVersion", "identifier")
                .and(sampleSizeCondition).as("sampleSize"),
            group("name")
                .sum("sampleSize").as("sampleSize")
                .sum("value").as("value")
                .first("viewId").as("viewId")
                .first("historicType").as("historicType")
                .first("collectorId").as("collectorId")
                .first("identifier").as("identifier")
                .first("appVersion").as("appVersion"),
            project("name", "sampleSize", "viewId", "value", "historicType", "collectorId", "identifier")
                .and(Divide.valueOf("value").divideBy("sampleSize")).as("value"));

        AggregationResults<HistoricUserMetricStats> groupResults =
            mongoTemplate.aggregate(aggregation,"historic_user_metrics", HistoricUserMetricStats.class);

        return groupResults.getMappedResults();
    }

    @Override
    public List<HistoricUserMetricStats> getUserMetricSumTotalForPeriod(List<String> views, ChronoUnit unit, List<String> metricNames, long timestamp){

        Cond sampleSizeCondition = ConditionalOperators.when(Criteria.where("sampleSize").gt(0))
            .thenValueOf("$sampleSize").otherwise(1l);

        TypedAggregation<HistoricUserMetric> aggregation = newAggregation(HistoricUserMetric.class,
            match(Criteria.where("viewId").in(views)
                .and("name").in(metricNames)
                .and("value").gte(0d)
                .and("historicType").is(unit)
                .and("timestamp").gte(timestamp)),
            project("viewId", "name", "value", "historicType", "sampleSize", "collectorId", "appVersion", "identifier")
                .and(ConditionalOperators.ifNull("sampleSize").then(1l)).as("sampleSize"),
            project("viewId", "name", "value", "historicType", "sampleSize", "collectorId", "appVersion", "identifier")
                .and(sampleSizeCondition).as("sampleSize"),
            group("name")
                .sum("sampleSize").as("sampleSize")
                .sum("value").as("value")
                .first("viewId").as("viewId")
                .first("historicType").as("historicType")
                .first("collectorId").as("collectorId")
                .first("identifier").as("identifier")
                .first("appVersion").as("appVersion"));

        AggregationResults<HistoricUserMetricStats> groupResults =
            mongoTemplate.aggregate(aggregation,"historic_user_metrics", HistoricUserMetricStats.class);

        return groupResults.getMappedResults();
    }

}
