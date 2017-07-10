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

import static com.bbva.arq.devops.ae.mirrorgate.core.utils.DashboardStatus.DELETED;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

public class DashboardRepositoryImpl implements DashboardRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<DashboardDTO> getActiveDashboards() {

        Aggregation aggregation = newAggregation(
                sort(new Sort(Sort.Direction.DESC, "lastModification")),
                group("name")
                        .first("name").as("name")
                        .first("displayName").as("displayName")
                        .first("status").as("status")
                        .first("logoUrl").as("logoUrl")
                        .first("adminUsers").as("adminUsers")
        );

        //Convert the aggregation result into a List
        AggregationResults<DashboardDTO> groupResults
                = mongoTemplate.aggregate(aggregation, Dashboard.class, DashboardDTO.class);

        return groupResults.getMappedResults().stream()
                .filter(dashboard -> dashboard.getStatus() != DELETED)
                .collect(Collectors.toList());
    }

}
