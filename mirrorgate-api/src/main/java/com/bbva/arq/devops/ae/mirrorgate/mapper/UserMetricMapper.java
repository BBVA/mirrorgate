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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;

public class UserMetricMapper {

    public static UserMetricDTO map(UserMetric source) {
        return map(source, new UserMetricDTO());
    }

    public static UserMetricDTO map(UserMetric source, UserMetricDTO target) {
        return target
                .setViewId(source.getViewId())
                .setAppVersion(source.getAppVersion())
                .setPlatform(Platform.toString(source.getPlatform()))
                .setName(source.getName())
                .setValue(source.getValue())
                .setTimestamp(source.getTimestamp())
                .setCollectorId(source.getCollectorId());
    }

    public static UserMetric map(UserMetricDTO source) {
        return map(source, new UserMetric());
    }

    public static UserMetric map(UserMetricDTO source, UserMetric target) {
        return target
                .setViewId(source.getViewId())
                .setAppVersion(source.getAppVersion())
                .setPlatform(Platform.fromString(source.getPlatform()))
                .setName(source.getName())
                .setValue(source.getValue())
                .setTimestamp(source.getTimestamp())
                .setCollectorId(source.getCollectorId());
    }

}
