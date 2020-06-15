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
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IssueRepository extends CrudRepository<Issue, String>, IssueRepositoryCustom {

    @Query(value = "{$or:[{projectName: {$in: ?0 }},{keywords:{$elemMatch:{$in: ?0 }}}], sprintAssetState: 'ACTIVE'}")
    List<Issue> findActiveUserStoriesByBoards(final List<String> boards, final Sort sort);

    List<Issue> findAllByTypeAndPiNames(final String type, final String piName);

    Issue findFirstByIssueIdAndCollectorId(final String issueId, final String collectorId);

    List<Issue> findAllByParentsKeysIn(final List<String> keys);

    List<Issue> findAllByNumberInAndType(final List<String> keys, final String type);

    void deleteByIssueIdAndCollectorId(final String id, final String collectorId);

    List<Issue> findByKeywordsInAndTypeAndStatusNot(final List<String> boards, final String type, final String status);
}
