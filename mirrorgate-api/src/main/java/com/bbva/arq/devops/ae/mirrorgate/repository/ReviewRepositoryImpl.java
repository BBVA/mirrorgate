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

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationReviewsDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.mongodb.BasicDBObject;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.IfNull;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * Reviews Repository
 */
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ApplicationDTO> getAverageRateByAppNames(List<String> names) {

        Aggregation aggregation = newAggregation(
            match(Criteria.where("appname").in(names).and("timestamp").exists(true)),
            sort(new Sort(DESC, "timestamp")),
            project("appname", "platform", "starrating",
                        "timestamp", "comment", "authorName")
                .and("amount").applyCondition(IfNull.ifNull("amount").then(1))
                .and("starrating").multiply(IfNull.ifNull("amount").then(1)).as("starrating_accumulated"),
            group("appname", "platform")
                .sum("amount").as("total_amount")
                .sum("starrating_accumulated").as("total_starrating")
                .push(new BasicDBObject("author", "$authorName")
                    .append("rate", "$starrating" )
                    .append("timestamp", "$timestamp")
                    .append("comment", "$comment")
                ).as("reviews"),
            project("appname", "platform")
                .and("total_starrating").as("ratingTotal")
                .and("total_amount").as("votesTotal")
                .and("reviews").slice(8, 0)
        );

        //Convert the aggregation result into a List
        AggregationResults<ApplicationDTO> groupResults
                = mongoTemplate.aggregate(aggregation, Review.class, ApplicationDTO.class);

        return groupResults.getMappedResults();
    }

    @Override
    public List<ApplicationReviewsDTO> getLastReviewPerApplication(List<String> names){

        Aggregation aggregation = newAggregation(
            match(Criteria.where("appname").in(names)),
                sort(new Sort(DESC, "timestamp")),
                group("appname", "platform")
                .first("platform").as("platform")
                .first("appname").as("appName")
                .first("appname").as("appId")
                .first("commentId").as("commentId")
        );

        //Convert the aggregation result into a List
        AggregationResults<ApplicationReviewsDTO> groupResults
            = mongoTemplate.aggregate(aggregation, Review.class, ApplicationReviewsDTO.class);

        return groupResults.getMappedResults();

    }

    @Override
    public List<ApplicationDTO> getAverageRateByAppNamesAfterTimestamp(List<String> names, Long timestamp) {

        Aggregation aggregation = newAggregation(
                match(Criteria.where("appname").in(names).and("timestamp").gte(timestamp)),
                group("appname", "platform")
                        .count().as("votes7Days")
                        .sum("starrating").as("rating7Days")
        );

        //Convert the aggregation result into a List
        AggregationResults<ApplicationDTO> groupResults
                = mongoTemplate.aggregate(aggregation, Review.class, ApplicationDTO.class);

        return groupResults.getMappedResults();

    }

}
