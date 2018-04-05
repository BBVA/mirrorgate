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

public class HistoricUserMetricStats {

    private String identifier;

    private String viewId;

    private String appVersion;

    private String platform;

    private String name;

    private long timestamp;

    private double value;

    private long sampleSize;

    /* Associated collector ID */
    private String collectorId;

    public String getIdentifier() {
        return identifier != null ? identifier : viewId
            + platform
            + appVersion
            + name
            + collectorId;
    }

    public HistoricUserMetricStats setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public String getViewId() {
        return viewId;
    }

    public HistoricUserMetricStats setViewId(String viewId) {
        this.viewId = viewId;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public HistoricUserMetricStats setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public HistoricUserMetricStats setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getName() {
        return name;
    }

    public HistoricUserMetricStats setName(String name) {
        this.name = name;
        return this;
    }

    public double getValue() {
        return value;
    }

    public HistoricUserMetricStats setValue(double value) {
        this.value = value;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public HistoricUserMetricStats setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public long getSampleSize() {
        return sampleSize;
    }

    public HistoricUserMetricStats setSampleSize(long sampleSize) {
        this.sampleSize = sampleSize;
        return this;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public HistoricUserMetricStats setCollectorId(String collectorId) {
        this.collectorId = collectorId;
        return this;
    }

    public HistoricUserMetricStats getInstance() {
        return this;
    }
}
