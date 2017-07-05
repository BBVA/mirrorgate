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

import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Features repository
 */
public interface FeatureRepository extends CrudRepository<Feature, ObjectId>, FeatureRepositoryCustom {

    @Query(value = "{$or:[{sProjectName: {$in: ?0 }},{keywords:{$elemMatch:{$in: ?0 }}}], sSprintAssetState: 'ACTIVE'}")
    List<Feature> findActiveUserStoriesByBoards(List<String> boards, Sort sort);

    List<Feature> findAllBysPiNamesIn(String piName);

    List<Feature> findAllBysIdInAndCollectorId(Iterable<String> ids, String collectorId);

    List<Feature> findAllBysParentKeyIn(List<String> keys);

    void deleteBysIdAndCollectorId(String id, String collectorId);

    List<Feature> findBySProjectNameInAndSTypeNameAndSStatusNot(List<String> boards, String type, String status);
}
