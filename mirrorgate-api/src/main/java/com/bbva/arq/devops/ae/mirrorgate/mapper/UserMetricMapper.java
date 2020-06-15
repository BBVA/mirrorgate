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

import com.bbva.arq.devops.ae.mirrorgate.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;

public class UserMetricMapper {

    public static UserMetricDTO map(final UserMetric source) {
        return map(source, new UserMetricDTO());
    }

    private static UserMetricDTO map(final UserMetric source, final UserMetricDTO target) {
        return target
            .setIdentifier(source.getIdentifier())
            .setViewId(source.getViewId())
            .setAppVersion(source.getAppVersion())
            .setPlatform(source.getPlatform())
            .setName(source.getName())
            .setValue(source.getValue())
            .setSampleSize(source.getSampleSize() == null ? 1 : source.getSampleSize())
            .setTimestamp(source.getTimestamp())
            .setCollectorId(source.getCollectorId());
    }

    public static UserMetric map(final UserMetricDTO source) {
        return map(source, new UserMetric());
    }

    private static UserMetric map(final UserMetricDTO source, final UserMetric target) {
        return target
            .setIdentifier(source.getIdentifier())
            .setViewId(source.getViewId())
            .setAppVersion(source.getAppVersion())
            .setPlatform(source.getPlatform())
            .setName(source.getName())
            .setValue(source.getValue())
            .setSampleSize(source.getSampleSize())
            .setTimestamp(source.getTimestamp())
            .setCollectorId(source.getCollectorId());
    }

    public static UserMetricDTO map(HistoricUserMetric source) {
        return new UserMetricDTO()
            .setIdentifier(source.getIdentifier())
            .setViewId(source.getViewId())
            .setAppVersion(source.getAppVersion())
            .setPlatform(source.getPlatform())
            .setName(source.getName())
            .setValue(source.getValue())
            .setSampleSize(source.getSampleSize())
            .setTimestamp(source.getTimestamp())
            .setCollectorId(source.getCollectorId());
    }

}
