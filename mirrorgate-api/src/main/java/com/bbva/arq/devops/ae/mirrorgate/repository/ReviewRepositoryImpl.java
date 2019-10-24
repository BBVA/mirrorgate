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

import com.bbva.arq.devops.ae.mirrorgate.dto.ApplicationDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.ApplicationReviewsDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

/**
 * Reviews Repository
 */
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ApplicationDTO> getAppInfoByAppNames(List<String> names) {

        Aggregation aggregation = newAggregation(
            match(Criteria.where("appname").in(names).and("timestamp").exists(true)),
            sort(Sort.by(DESC, "timestamp")),
            project("appname", "platform", "starrating",
                        "timestamp", "comment", "authorName","url"),
            group("appname", "platform")
                .push(new BasicDBObject("author", "$authorName")
                    .append("rate", "$starrating" )
                    .append("timestamp", "$timestamp")
                    .append("comment", "$comment")
                    .append("url", "$url")
                ).as("reviews"),
            project("appname", "platform")
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
            sort(Sort.by(DESC, "timestamp")),
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
                        .count().as("votesShortTerm")
                        .sum("starrating").as("ratingShortTerm")
        );

        //Convert the aggregation result into a List
        AggregationResults<ApplicationDTO> groupResults
                = mongoTemplate.aggregate(aggregation, Review.class, ApplicationDTO.class);

        return groupResults.getMappedResults();

    }

}
