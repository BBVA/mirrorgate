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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.SprintStats;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Ceil;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Divide;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Subtract;
import org.springframework.data.mongodb.core.query.Criteria;

public class FeatureRepositoryImpl implements FeatureRepositoryCustom{

    @Autowired
    MongoTemplate mongoTemplate;

    private static class DoubleValue {
        Double value;
    }

    @Override
    public Double getBacklogEstimateByKeywords(List<String> boards) {

        Aggregation agg = newAggregation(
                match(Criteria
                        .where("keywords").in(boards)
                        .and("sSprintAssetState").ne("ACTIVE")
                        .and("sTypeName").in(Arrays.asList("Story","Bug"))
                        .and("sStatus").nin(Arrays.asList("Accepted", "Done"))
                ),
                group()
                        .sum("dEstimate")
                        .as("value"),
                project("value")
                        .andExclude("_id")
        );


        AggregationResults<DoubleValue> groupResults
                = mongoTemplate.aggregate(agg, "feature", DoubleValue.class);
        DoubleValue val = groupResults.getUniqueMappedResult();

        return val == null ? 0 : val.value;
    }

    @Override
    public SprintStats getSprintStatsByKeywords(List<String> boards) {

        Aggregation agg = newAggregation(
                match(Criteria
                        .where("keywords").in(boards)
                        .and("sSprintAssetState").is("CLOSED")
                        .and("sTypeName").in(Arrays.asList("Story","Bug"))
                        .and("sStatus").in(Arrays.asList("Accepted", "Done"))
                ),
                group("sSprintName")
                        .first("sprintBeginDate").as("start")
                        .first("sprintEndDate").as("end")
                        .sum("dEstimate").as("estimate")
                ,
                group()
                        .avg("estimate").as("estimateAvg")
                        .avg(
                                Ceil.ceilValueOf(
                                    Divide.valueOf(
                                            Subtract.valueOf("end").subtract("start")
                                    ).divideBy(3600 * 1000 * 24)
                            )
                        ).as("daysDurationAvg"),
                project("daysDurationAvg","estimateAvg")
                        .andExclude("_id")
        );

        AggregationResults<SprintStats> groupResults
                = mongoTemplate.aggregate(agg, "feature", SprintStats.class);
        return groupResults.getUniqueMappedResult();

    }

    @Override
    public PINamesAggregationResult getProductIncrementFromFeatures(List<String> boards) {

        Aggregation agg = newAggregation(
            match(Criteria
                .where("keywords").in(boards)
                .and("sTypeName").is("Feature")
            ),
            project("sPiNames")
                .andExclude("_id"),
            unwind("sPiNames"),
            group()
                .addToSet("sPiNames")
                .as("piNames")
        );

        AggregationResults<PINamesAggregationResult> aggregationResult
            = mongoTemplate.aggregate(agg, "feature", PINamesAggregationResult.class);

        return aggregationResult.getUniqueMappedResult();
    }

    public static class PINamesAggregationResult{

        private List<String> piNames;

        public PINamesAggregationResult(List<String> piNames){
            this.piNames = piNames;
        }

        public List<String> getPiNames() {
            return piNames;
        }

        public void setPiNames(List<String> piNames) {
            this.piNames = piNames;
        }
    }

}
