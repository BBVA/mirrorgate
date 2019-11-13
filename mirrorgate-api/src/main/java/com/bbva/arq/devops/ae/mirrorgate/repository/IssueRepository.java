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

import com.bbva.arq.devops.ae.mirrorgate.model.Issue;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IssueRepository extends CrudRepository<Issue, String>, IssueRepositoryCustom {

    @Query(value = "{$or:[{projectName: {$in: ?0 }},{keywords:{$elemMatch:{$in: ?0 }}}], sprintAssetState: 'ACTIVE'}")
    List<Issue> findActiveUserStoriesByBoards(List<String> boards, Sort sort);

    List<Issue> findAllByTypeAndPiNames(String type, String piName);

    Issue findFirstByIssueIdAndCollectorId(String issueId, String collectorId);

    List<Issue> findAllByParentsKeysIn(List<String> keys);

    List<Issue> findAllByNumberInAndType(List<String> keys, String type);

    void deleteByIssueIdAndCollectorId(String id, String collectorId);

    List<Issue> findByKeywordsInAndTypeAndStatusNot(List<String> boards, String type, String status);
}
