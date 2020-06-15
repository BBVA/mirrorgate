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

import java.time.temporal.ChronoUnit;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "historic_user_metrics")
@CompoundIndex(name = "tendency", def = "{'viewId' : 1, 'historicType': 1, 'timestamp': 1}")
public class HistoricUserMetric implements BaseModel {

    @Id
    private String id;

    private String identifier;

    private String viewId;

    private String appVersion;

    private String platform;

    private String name;

    private Double value;

    private Long sampleSize;

    private Long timestamp;

    /* Associated collector ID */
    private String collectorId;

    private ChronoUnit historicType;

    public String getIdentifier() {
        return identifier;
    }

    public HistoricUserMetric setIdentifier(final String identifier) {
        this.identifier = identifier;
        return this;
    }

    public String getViewId() {
        return viewId;
    }

    public HistoricUserMetric setViewId(final String viewId) {
        this.viewId = viewId;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public HistoricUserMetric setAppVersion(final String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public HistoricUserMetric setPlatform(final String platform) {
        this.platform = platform;
        return this;
    }

    public String getName() {
        return name;
    }

    public HistoricUserMetric setName(final String name) {
        this.name = name;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public HistoricUserMetric setValue(final Double value) {
        this.value = value;
        return this;
    }

    public Long getSampleSize() {
        return sampleSize;
    }

    public HistoricUserMetric setSampleSize(final Long sampleSize) {
        this.sampleSize = sampleSize;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public HistoricUserMetric setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public HistoricUserMetric setCollectorId(final String collectorId) {
        this.collectorId = collectorId;
        return this;
    }

    public ChronoUnit getHistoricType() {
        return historicType;
    }

    public HistoricUserMetric setHistoricType(final ChronoUnit historicType) {
        this.historicType = historicType;
        return this;
    }

    @Override
    public Object getId() {
        return this.id;
    }

    public HistoricUserMetric setId(final String id) {
        this.id = id;
        return this;
    }
}
