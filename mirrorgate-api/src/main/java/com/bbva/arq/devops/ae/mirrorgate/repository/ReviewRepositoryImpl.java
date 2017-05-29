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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationReviewsDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
            match(Criteria.where("appname").all(names)),
            sort(new Sort("timestamp")),
            group("appname", "platform")
                .avg("starrating").as("rate")
                .last("authorName").as("last_review_author")
                .last("starrating").as("last_review_rate")
                .last("timestamp").as("last_review_timestamp")
                .last("comment").as("last_review_comment")
        );

        //Convert the aggregation result into a List
        AggregationResults<ApplicationDTO> groupResults
                = mongoTemplate.aggregate(aggregation, Review.class, ApplicationDTO.class);

        return groupResults.getMappedResults();
    }

    public List<ApplicationReviewsDTO> getLastReviewPerApplication(List<String> names){

        Aggregation aggregation = newAggregation(
            match(Criteria.where("appname").in(names)),
            sort(new Sort("commentId")),
            group("appname", "platform")
                .last("platform").as("platform")
                .last("appname").as("appName")
                .last("appname").as("appId")
                .last("commentId").as("commentId")
        );

        //Convert the aggregation result into a List
        AggregationResults<ApplicationReviewsDTO> groupResults
            = mongoTemplate.aggregate(aggregation, Review.class, ApplicationReviewsDTO.class);

        return groupResults.getMappedResults();

    }

}
