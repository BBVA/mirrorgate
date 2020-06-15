/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.mapper;

import com.bbva.arq.devops.ae.mirrorgate.dto.CommitDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Commit;

public class CommitMapper {

    public static CommitDTO map(final Commit source) {
        return map(source, new CommitDTO());
    }

    private static CommitDTO map(final Commit source, final CommitDTO target) {
        return target
            .setHash(source.getHash())
            .setRepository(source.getRepository())
            .setTimestamp(source.getTimestamp())
            .setMessage(source.getMessage())
            .setAuthorName(source.getAuthorName())
            .setAuthorEmail(source.getAuthorEmail())
            .setCommitterName(source.getCommitterName())
            .setCommitterEmail(source.getCommitterEmail())
            .setParentsIds(source.getParentsIds())
            .setBranches(source.getBranches());
    }

    public static Commit map(final CommitDTO source) {
        return map(source, new Commit());
    }

    private static Commit map(final CommitDTO source, final Commit target) {
        return target
            .setHash(source.getHash())
            .setRepository(source.getRepository())
            .setTimestamp(source.getTimestamp())
            .setMessage(source.getMessage())
            .setAuthorName(source.getAuthorName())
            .setAuthorEmail(source.getAuthorEmail())
            .setCommitterName(source.getCommitterName())
            .setCommitterEmail(source.getCommitterEmail())
            .setParentsIds(source.getParentsIds())
            .setBranches(source.getBranches());
    }

}
