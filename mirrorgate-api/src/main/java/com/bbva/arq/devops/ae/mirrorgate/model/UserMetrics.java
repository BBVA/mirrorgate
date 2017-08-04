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

package com.bbva.arq.devops.ae.mirrorgate.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by alfonso on 27/07/17.
 */
@Document(collection = "user-metrics")
public class UserMetrics extends BaseModel {

    @Indexed
    private String viewId;

    private Long rtActiveUsers;

    private Long weekUsersCount;

    public String getViewId() {
        return viewId;
    }

    public UserMetrics setViewId(String viewId) {
        this.viewId = viewId;
        return this;
    }

    public Long getRtActiveUsers() {
        return rtActiveUsers;
    }

    public UserMetrics setRtActiveUsers(Long rtActiveUsers) {
        this.rtActiveUsers = rtActiveUsers;
        return this;
    }

    public Long getWeekUsersCount() {
        return weekUsersCount;
    }

    public UserMetrics setWeekUsersCount(Long weekUsersCount) {
        this.weekUsersCount = weekUsersCount;
        return this;
    }
}
