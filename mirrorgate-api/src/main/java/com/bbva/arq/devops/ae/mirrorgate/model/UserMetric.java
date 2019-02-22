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

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user-metrics")
public class UserMetric implements BaseModel {

    @Id
    private String id;
    private String identifier;

    @Indexed
    private String viewId;

    private String appVersion;

    private String platform;

    private String name;

    private Double value;

    private Long sampleSize;

    private Long timestamp;

    @Indexed
    private String collectorId; // associated collector ID

    public String getIdentifier() {
        return identifier;
    }

    public UserMetric setIdentifier(String identifier) {
        this.identifier = identifier;
        this.id = identifier;
        return this;
    }

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

    public String getPlatform() {
        return platform;
    }

    public UserMetric setPlatform(String platform) {
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

    public Double getValue() {
        return value;
    }

    public UserMetric setValue(Double value) {
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

    public Long getSampleSize() {
        return sampleSize;
    }

    public UserMetric setSampleSize(Long sampleSize) {
        this.sampleSize = sampleSize;
        return this;
    }

    @Override
    public Object getId() {
        return this.id;
    }
}
