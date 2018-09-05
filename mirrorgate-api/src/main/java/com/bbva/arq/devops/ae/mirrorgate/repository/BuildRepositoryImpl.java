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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.bbva.arq.devops.ae.mirrorgate.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.support.BuildStatus;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

public class BuildRepositoryImpl implements BuildRepositoryCustom {

    private static class BuildStatsEntry {
        BuildStatus buildStatus;
        Long count;
        Double duration;
    }

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Build> findLastBuildsByKeywordsAndByTeamMembers(List<String> keywords, List<String> teamMembers) {
        TypedAggregation<Build> agg = newAggregation(Build.class,
                match(Criteria.where("buildStatus")
                        .nin(
                                BuildStatus.Aborted.toString(),
                                BuildStatus.NotBuilt.toString(),
                                BuildStatus.Unknown.toString(),
                                BuildStatus.Deleted.toString()
                        )
                        .orOperator(
                                Criteria.where("timestamp")
                                .gt(System.currentTimeMillis() - 24 * 3600 * 1000),
                                Criteria.where("latest").is(true)
                                .and("buildStatus").ne(
                                BuildStatus.Deleted.toString())
                        )
                        .andOperator(
                                getCriteriaExpressionsForKeywords(keywords),
                                getCriteriaExpressionsForTeamMembers(teamMembers)
                        )
                ),
                //Avoid Mongo to "optimize" the sort operation... Why Mongo, oh why?!
                project("buildStatus", "branch", "projectName", "repoName",
                        "timestamp", "buildUrl", "duration", "startTime",
                        "endTime", "culprits")
                .andExclude("_id"),
                sort(Sort.Direction.ASC, "timestamp"),
                group("branch", "repoName", "projectName")
                        .last("buildStatus").as("buildStatus")
                        .last("repoName").as("repoName")
                        .last("timestamp").as("timestamp")
                        .last("buildUrl").as("buildUrl")
                        .last("branch").as("branch")
                        .last("startTime").as("startTime")
                        .last("endTime").as("endTime")
                        .last("duration").as("duration")
                        .last("projectName").as("projectName")
                        .last("culprits").as("culprits"),
                project("buildStatus", "branch", "projectName", "repoName",
                        "timestamp", "buildUrl", "duration", "startTime",
                        "endTime", "culprits")
                .andExclude("_id")
        );

        //Convert the aggregation result into a List
        AggregationResults<Build> groupResults
                = mongoTemplate.aggregate(agg, Build.class);

        return groupResults.getMappedResults();

    }

    @Override
    public Map<BuildStatus, BuildStats> getBuildStatusStatsAfterTimestamp(List<String> keywords, List<String> teamMembers, Long timestamp) {
        Aggregation agg = newAggregation(
                match(Criteria
                        .where("timestamp").gt(timestamp)
                        .and("buildStatus")
                            .nin(BuildStatus.Aborted.toString(),
                                    BuildStatus.NotBuilt.toString(),
                                    BuildStatus.Deleted.toString(),
                                    BuildStatus.Unknown.toString(),
                                    BuildStatus.InProgress.toString()
                            )
                        .andOperator(
                                getCriteriaExpressionsForKeywords(keywords),
                                getCriteriaExpressionsForTeamMembers(teamMembers)
                        )
                ),
                group("buildStatus")
                        .last("buildStatus").as("buildStatus")
                        .count().as("count")
                        .avg("duration").as("duration"),
                project("buildStatus","count","duration")
                        .andExclude("_id")
        );

        AggregationResults<BuildStatsEntry> groupResults
                = mongoTemplate.aggregate(agg, "builds", BuildStatsEntry.class);

        List<BuildStatsEntry> statEntries = groupResults.getMappedResults();

        Map<BuildStatus, BuildStats> result = new EnumMap<>(BuildStatus.class);

        statEntries.forEach(
                stat -> result.put(stat.buildStatus,
                            new BuildStats()
                                .setCount(stat.count)
                                .setDuration(stat.duration)
                                .setFailureRate(stat.buildStatus
                                    == BuildStatus.Failure ? 100L : 0)
                )
        );

        return result;
    }

    private Criteria getCriteriaExpressionsForKeywords(List<String> keywords) {
        return Criteria
            .where("keywords")
            .in(keywords.stream().map(kw -> Pattern.compile("^" + kw + "$")).toArray());
    }

    private Criteria getCriteriaExpressionsForTeamMembers(List<String> teamMembers) {
        if (teamMembers == null) {
            return new Criteria();
        }

        return Criteria
            .where("culprits")
            .in(teamMembers);
    }
}
