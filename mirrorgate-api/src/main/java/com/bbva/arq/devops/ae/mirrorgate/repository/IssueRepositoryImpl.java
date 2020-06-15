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

import com.bbva.arq.devops.ae.mirrorgate.dto.SprintStats;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueStatus;
import com.bbva.arq.devops.ae.mirrorgate.support.IssueType;
import com.bbva.arq.devops.ae.mirrorgate.utils.MirrorGateUtils.DoubleValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Ceil;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Divide;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Subtract;
import org.springframework.data.mongodb.core.query.Criteria;

public class IssueRepositoryImpl implements IssueRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public double getBacklogEstimateByKeywords(final List<String> boards) {

        final Aggregation agg = newAggregation(
            match(Criteria
                .where("keywords").in(boards)
                .and("sprintAssetState").ne("ACTIVE")
                .and("type").in(Arrays.asList(IssueType.STORY.getName(), IssueType.BUG.getName()))
                .and("status").nin(Arrays.asList(IssueStatus.ACCEPTED.getName(), IssueStatus.DONE.getName()))
            ),
            group()
                .sum("estimation")
                .as("value"),
            project("value")
                .andExclude("_id")
        );

        final AggregationResults<DoubleValue> groupResults
            = mongoTemplate.aggregate(agg, "issue", DoubleValue.class);
        final DoubleValue val = groupResults.getUniqueMappedResult();

        return val == null ? 0 : val.value;
    }

    @Override
    public SprintStats getSprintStatsByKeywords(final List<String> boards) {

        final Aggregation agg = newAggregation(
            match(Criteria
                .where("keywords").in(boards)
                .and("sprintAssetState").is("CLOSED")
                .and("type").in(Arrays.asList(IssueType.STORY.getName(), IssueType.BUG.getName()))
                .and("status").in(Arrays.asList(IssueStatus.ACCEPTED.getName(), IssueStatus.DONE.getName()))
            ),
            group("sprintName")
                .first("sprintBeginDate").as("start")
                .first("sprintEndDate").as("end")
                .sum("estimation").as("estimate"),
            group()
                .avg("estimate").as("estimateAvg")
                .avg(
                    Ceil.ceilValueOf(
                        Divide.valueOf(
                            Subtract.valueOf("end").subtract("start")
                        ).divideBy(3600 * 1000 * 24)
                    )
                ).as("daysDurationAvg"),
            project("daysDurationAvg", "estimateAvg")
                .andExclude("_id")
        );

        final AggregationResults<SprintStats> groupResults
            = mongoTemplate.aggregate(agg, "issue", SprintStats.class);

        return groupResults.getUniqueMappedResult();
    }

    @Override
    public List<String> programIncrementBoardFeatures(
        final List<String> boards,
        final List<String> programIncrementFeatures
    ) {

        final Aggregation agg = newAggregation(
            match(Criteria
                .where("parentsKeys").in(programIncrementFeatures)
                .and("keywords").in(boards)
            ),
            unwind("parentsKeys"),
            group()
                .addToSet("parentsKeys")
                .as("features"),
            project("features")
                .andExclude("_id")
        );

        final AggregationResults<ProgramIncrementBoardFeatures> aggregationResult
            = mongoTemplate.aggregate(agg, "issue", ProgramIncrementBoardFeatures.class);

        return aggregationResult.getUniqueMappedResult() != null ? aggregationResult.getUniqueMappedResult().features
            : new ArrayList<>();
    }

    @Override
    public ProgramIncrementNamesAggregationResult getProductIncrementFromPiPattern(final Pattern pi) {

        final Aggregation agg = newAggregation(
            match(Criteria
                .where("type").is(IssueType.FEATURE.getName())
            ),
            project("piNames").andExclude("_id"),
            unwind("piNames"),
            match(Criteria
                .where("piNames").is(pi)
            ),
            group().addToSet("piNames").as("piNames")
        );

        final AggregationResults<ProgramIncrementNamesAggregationResult> aggregationResult
            = mongoTemplate.aggregate(agg, "issue", ProgramIncrementNamesAggregationResult.class);

        return aggregationResult.getUniqueMappedResult();
    }

    private static class ProgramIncrementBoardFeatures {
        private final List<String> features;

        ProgramIncrementBoardFeatures(final List<String> features) {
            this.features = features;
        }
    }

    public static class ProgramIncrementNamesAggregationResult {

        private List<String> piNames;

        public ProgramIncrementNamesAggregationResult(final List<String> piNames) {
            this.piNames = piNames;
        }

        public List<String> getPiNames() {
            return piNames;
        }

        public ProgramIncrementNamesAggregationResult setPiNames(final List<String> piNames) {
            this.piNames = piNames;
            return this;
        }
    }

}
