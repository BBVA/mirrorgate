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

import com.bbva.arq.devops.ae.mirrorgate.dto.SprintStats;
import com.bbva.arq.devops.ae.mirrorgate.utils.MirrorGateUtils.DoubleValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Ceil;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Divide;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Subtract;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

public class FeatureRepositoryImpl implements FeatureRepositoryCustom{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public double getBacklogEstimateByKeywords(List<String> boards) {

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
    public List<String> programIncrementBoardFeatures(List<String> boards, List<String> programIncrementFeatures){

        Aggregation agg = newAggregation(
            match(Criteria
                .where("sParentKey").in(programIncrementFeatures)
                .and("keywords").in(boards)
            ),
            unwind("sParentKey"),
            group()
                .addToSet("sParentKey")
                .as("features"),
            project("features")
                .andExclude("_id")
        );

        AggregationResults<ProgramIncrementBoardFeatures> aggregationResult
            = mongoTemplate.aggregate(agg, "feature", ProgramIncrementBoardFeatures.class);

        return aggregationResult.getUniqueMappedResult() != null ? aggregationResult.getUniqueMappedResult().features : new ArrayList<>();
    }

    @Override
    public ProgramIncrementNamesAggregationResult getProductIncrementFromPiPattern(Pattern pi) {

        Aggregation agg = newAggregation(
            match(Criteria
                    .where("sTypeName").is("Feature")
            ),
            project("sPiNames").andExclude("_id"),
            unwind("sPiNames"),
            match(Criteria
                .where("sPiNames").is(pi)
            ),
            group().addToSet("sPiNames").as("piNames")
        );

        AggregationResults<ProgramIncrementNamesAggregationResult> aggregationResult
            = mongoTemplate.aggregate(agg, "feature", ProgramIncrementNamesAggregationResult.class);

        return aggregationResult.getUniqueMappedResult();
    }

    private static class ProgramIncrementBoardFeatures {
        private final List<String> features;

        ProgramIncrementBoardFeatures(List<String> features){
            this.features = features;
        }
    }

    public static class ProgramIncrementNamesAggregationResult {

        private List<String> piNames;

        public ProgramIncrementNamesAggregationResult(List<String> piNames){
            this.piNames = piNames;
        }

        public List<String> getPiNames() {
            return piNames;
        }

        public ProgramIncrementNamesAggregationResult setPiNames(List<String> piNames) {
            this.piNames = piNames;
            return this;
        }
    }

}
