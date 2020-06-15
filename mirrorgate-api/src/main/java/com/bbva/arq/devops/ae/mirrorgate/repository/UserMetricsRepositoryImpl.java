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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

public class UserMetricsRepositoryImpl implements UserMetricsRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<UserMetric> findAllStartingWithViewId(final List<String> viewIds) {

        final Aggregation agg = newAggregation(
            match(getCriteriaExpressionsForUserMetrics(viewIds))
        );

        final AggregationResults<UserMetric> aggregationResult
            = mongoTemplate.aggregate(agg, UserMetric.class, UserMetric.class);

        return aggregationResult.getMappedResults();
    }

    private Criteria getCriteriaExpressionsForUserMetrics(final List<String> viewIds) {
        return Criteria
            .where("viewId")
            .in(viewIds.stream().map(id -> Pattern.compile("^" + id + ".*$")).toArray());
    }

}
