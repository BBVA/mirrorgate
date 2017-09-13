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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.UserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user-metrics")
public class UserMetric extends BaseModel {

    @Indexed
    private String viewId;

    private String appVersion;

    private Platform platform;

    private String name;

    private String value;

    private Long timestamp;

    /* Associated collector ID */
    private String collectorId;

    public String getViewId() {
        return viewId;
    }

    public UserMetric setViewId(String viewId) {
        this.viewId = viewId;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public UserMetric setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public Platform getPlatform() {
        return platform;
    }

    public UserMetric setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserMetric setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public UserMetric setValue(String value) {
        this.value = value;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public UserMetric setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public UserMetric setCollectorId(String collectorId) {
        this.collectorId = collectorId;
        return this;
    }

    public boolean isTheSame(UserMetricDTO metric) {
        return viewId.equals(metric.getViewId())
                && equalsWithNulls(collectorId, metric.getCollectorId())
                && equalsWithNulls(appVersion, metric.getAppVersion())
                && equalsWithNulls(platform, Platform.fromString(metric.getPlatform()))
                && equalsWithNulls(name, metric.getName());
    }

    private static boolean equalsWithNulls(Object a, Object b) {
        if (a == b) {
            return true;
        }
        if ((a == null) || (b == null)) {
            return false;
        }
        return a.equals(b);
    }

}
