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

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import java.time.temporal.ChronoUnit;

public class HistoricUserMetricMapper {

    public static HistoricUserMetric mapToHistoric(
        final UserMetric origin,
        final ChronoUnit unit,
        final long periodTimestamp
    ) {
        return new HistoricUserMetric()
            .setId(generateId(origin.getIdentifier(), unit, periodTimestamp))
            .setIdentifier(origin.getIdentifier())
            .setHistoricType(unit)
            .setTimestamp(periodTimestamp)
            .setSampleSize(origin.getSampleSize())
            .setAppVersion(origin.getAppVersion())
            .setCollectorId(origin.getCollectorId())
            .setName(origin.getName())
            .setPlatform(origin.getPlatform())
            .setValue(origin.getValue())
            .setViewId(origin.getViewId());
    }

    public static String generateId(String identifier, ChronoUnit type, long periodTimestamp) {
        return identifier + type + periodTimestamp;
    }
}
