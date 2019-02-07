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

import com.bbva.arq.devops.ae.mirrorgate.dto.BuildStats;
import com.bbva.arq.devops.ae.mirrorgate.model.BuildSummary;
import com.bbva.arq.devops.ae.mirrorgate.support.BuildStatus;

class BuildSummaryMapper {

    private BuildSummaryMapper() {
    }

    public static BuildStats map(BuildSummary source) {
        return map(source, new BuildStats());
    }

    private static BuildStats map(BuildSummary source, BuildStats target) {
        return target
            .setCount(source.getTotalBuilds())
            .setDuration(source.getTotalDuration() != null && source.getTotalBuilds() != null ? source.getTotalDuration() : .0)
            .setFailureRate(source.getStatusMap().get(BuildStatus.Failure) == null ? 0
                : 100 * source.getStatusMap().get(BuildStatus.Failure) / (double) source.getTotalBuilds());

    }

}
