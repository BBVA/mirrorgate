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

import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

/**
 * Builds repository.
 */
public interface BuildRepository extends CrudRepository<Build, ObjectId>, BuildRepositoryCustom {

    Build findByBuildUrl(String buildUrl);

    List<Build> findAllByRepoNameAndProjectNameAndBranchAndLatestIsTrue(String repoName, String projectName, String branch);

    List<Build> findByIdIn(List<ObjectId> buildIds);

    List<Build> findAllByTimestampAfter(long timestamp);
}
