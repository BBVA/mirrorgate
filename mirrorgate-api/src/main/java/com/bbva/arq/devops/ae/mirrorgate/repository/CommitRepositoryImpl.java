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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.query.Criteria;

public class CommitRepositoryImpl implements CommitRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static class DoubleValue {
        Double value;
    }

    @Override
    public Integer getTimeToMaster(String repository) {

        Aggregation agg = newAggregation(
                match(Criteria
                    .where("timestamp").gte(System.currentTimeMillis()-30*24*3600)
                ),
                group()
                    .avg(ArithmeticOperators.Subtract.valueOf("$branches.refs/remotes/origin/master").subtract("timestamp")),
                project("timetomaster")
                        .andExclude("_id")
        );

    /*{$group: {_id: "time-to-master", timetomaster: {$avg: { $subtract: [ "$branches.refs/remotes/origin/master", "$timestamp" ] } } } }
])*/

        AggregationResults<Integer> timetomaster
                = mongoTemplate.aggregate(agg, "feature", DoubleValue.class);
        DoubleValue val = groupResults.getUniqueMappedResult();

        return val == null ? 0 : val.value;
    }

}
