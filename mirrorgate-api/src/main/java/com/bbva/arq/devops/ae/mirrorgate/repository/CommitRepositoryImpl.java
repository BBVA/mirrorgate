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

import com.bbva.arq.devops.ae.mirrorgate.utils.MirrorGateUtils.DoubleValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Ceil;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Subtract;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.concurrent.TimeUnit;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class CommitRepositoryImpl implements CommitRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Double getTimeToMaster(String repository, int daysBefore) {

        long now = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long before = now - (daysBefore * 60 * 60 * 24);

        Aggregation agg = newAggregation(
            match(Criteria
                .where("timestamp").gte(before)
            ),
            group()
                .avg(
                    Ceil.ceilValueOf(
                        Subtract.valueOf("$branches.refs/remotes/origin/master").subtract("timestamp")
                    )
                ).as("value"),
            project("value")
                .andExclude("_id")
        );

        AggregationResults<DoubleValue> timetomaster
                = mongoTemplate.aggregate(agg, "commits", DoubleValue.class);
        DoubleValue val = timetomaster.getUniqueMappedResult();

        return val == null || val.value == null ? 0 : val.value;
    }

}
