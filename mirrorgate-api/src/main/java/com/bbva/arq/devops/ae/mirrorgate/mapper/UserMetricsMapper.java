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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.UserMetricsDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetrics;

/**
 * Created by alfonso on 27/07/17.
 */
public class UserMetricsMapper {

    public static UserMetricsDTO map(UserMetrics source) {
        return map(source, new UserMetricsDTO());
    }

    public static UserMetricsDTO map(UserMetrics source, UserMetricsDTO target) {
        return target
                .setRtActiveUsers(source.getRtActiveUsers())
                .setViewId(source.getViewId())
                .setWeekUsersCount(source.getWeekUsersCount());
    }

    public static UserMetrics map(UserMetricsDTO source) {
        return map(source, new UserMetrics());
    }

    public static UserMetrics map(UserMetricsDTO source, UserMetrics target) {
        return target
                .setRtActiveUsers(source.getRtActiveUsers())
                .setViewId(source.getViewId())
                .setWeekUsersCount(source.getWeekUsersCount());
    }

}
