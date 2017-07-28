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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.BuildStatus;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
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
    public List<Build> findAllBranchesLastByReposName(List<String> repos) {
        TypedAggregation<Build> agg = newAggregation(Build.class,
                match(Criteria.where("buildStatus")
                        .nin(BuildStatus.Aborted.toString(),
                                BuildStatus.NotBuilt.toString(),
                                BuildStatus.Unknown.toString()
                        )
                        .orOperator(
                                Criteria.where("timestamp").gt(System.currentTimeMillis() - 24* 3600 * 1000),
                                Criteria.where("latest").is(true)
                                        .and("buildStatus").ne(BuildStatus.Deleted)
                        )),
                match(new Criteria().orOperator(getCriteriaExpressionsForRepos(repos))),
                //Avoid Mongo to "optimize" the sort operation.... Why Mongo, oh why?!
                project("buildStatus","branch","projectName","repoName","timestamp","buildUrl","duration","startTime","endTime")
                        .andExclude("_id"),
                sort(Sort.Direction.ASC, "timestamp"),
                group("branch","repoName","projectName")
                        .last("buildStatus").as("buildStatus")
                        .last("repoName").as("repoName")
                        .last("timestamp").as("timestamp")
                        .last("buildUrl").as("buildUrl")
                        .last("branch").as("branch")
                        .last("startTime").as("startTime")
                        .last("endTime").as("endTime")
                        .last("duration").as("duration")
                        .last("projectName").as("projectName"),
                match(Criteria.where("buildStatus").ne(BuildStatus.Deleted)),
                project("buildStatus","branch","projectName","repoName","timestamp","buildUrl","duration","startTime","endTime")
                        .andExclude("_id")
        );

        //Convert the aggregation result into a List
        AggregationResults<Build> groupResults
                = mongoTemplate.aggregate(agg, Build.class);

        return groupResults.getMappedResults();


    }

    @Override
    public Map<BuildStatus, BuildStats> getBuildStatusStatsAfterTimestamp(List<String> repos, Long timestamp) {
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
                        .orOperator(getCriteriaExpressionsForRepos(repos))
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

        statEntries.forEach(stat ->
            result.put(stat.buildStatus,
                    new BuildStats()
                            .setCount(stat.count)
                            .setDuration(stat.duration)
                            .setFailureRate(stat.buildStatus == BuildStatus.Failure ? 100L: 0)
            )
        );

        return result;
    }

    private Criteria[] getCriteriaExpressionsForRepos(List<String> repos) {
        List<Criteria> regExs = new ArrayList<>();
        repos.forEach((String repo) -> {
            regExs.add(Criteria.where("repoName").is(Pattern.compile("^" + repo + "$")));
            regExs.add(Criteria.where("projectName").is(Pattern.compile("^" + repo + "$")));
        });
        return regExs.toArray(new Criteria[regExs.size()]);
    }
}
