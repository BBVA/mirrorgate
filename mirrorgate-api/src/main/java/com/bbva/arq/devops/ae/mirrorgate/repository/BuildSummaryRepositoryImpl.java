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

import com.bbva.arq.devops.ae.mirrorgate.model.BuildSummary;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class BuildSummaryRepositoryImpl implements BuildSummaryRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<BuildSummary> findAllWithKeywordsAndTimestampAfter(List<String> keywords, Long timestamp) {
        Query query = new Query()
                .addCriteria(getCriteriaExpressionsForKeywords(keywords))
                .addCriteria(Criteria.where("timestamp").gte(timestamp));
        return mongoTemplate.find(query, BuildSummary.class);
    }

    private Criteria getCriteriaExpressionsForKeywords(List<String> keywords) {
        List<Criteria> regExs = new ArrayList<>();
        keywords.forEach((String kw) -> {
            regExs.add(Criteria.where("projectName")
                    .in(Pattern.compile("^" + kw + "$")));
            regExs.add(Criteria.where("repoName")
                    .in(Pattern.compile("^" + kw + "$")));
        });
        return new Criteria()
                .orOperator(regExs.toArray(new Criteria[0]));
    }

}
