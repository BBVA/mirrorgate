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

package com.bbva.arq.devops.ae.mirrorgate.core.dto;

public class UserMetricDTO {

    private String viewId;

    private String appVersion;

    private String platform;

    private String name;

    private Double value;

    private Long timestamp;

    /* Associated collector ID */
    private String collectorId;

    public String getViewId() {
        return viewId;
    }

    public UserMetricDTO setViewId(String viewId) {
        this.viewId = viewId;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public UserMetricDTO setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public UserMetricDTO setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserMetricDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public UserMetricDTO setValue(Double value) {
        this.value = value;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public UserMetricDTO setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public UserMetricDTO setCollectorId(String collectorId) {
        this.collectorId = collectorId;
        return this;
    }

}
