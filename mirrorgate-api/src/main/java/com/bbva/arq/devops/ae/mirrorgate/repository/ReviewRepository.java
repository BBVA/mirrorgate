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

import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Reviews repository.
 */
public interface ReviewRepository extends CrudRepository<Review, ObjectId>, ReviewRepositoryCustom {

    @Query(value = "{timestamp: null}")
    List<Review> findAllHistorical();

    @Query(value = "{appname: {$in: ?0}, timestamp: null}")
    List<Review> findHistoricalForApps(List<String> appname);

    List<Review> findAllByCommentIdIn(List<String> commentIds);

}
