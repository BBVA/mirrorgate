package com.bbva.arq.devops.ae.mirrorgate.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.temporal.ChronoUnit;

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

    public HistoricUserMetric setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public String getViewId() {
        return viewId;
    }

    public HistoricUserMetric setViewId(String viewId) {
        this.viewId = viewId;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public HistoricUserMetric setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public HistoricUserMetric setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getName() {
        return name;
    }

    public HistoricUserMetric setName(String name) {
        this.name = name;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public HistoricUserMetric setValue(Double value) {
        this.value = value;
        return this;
    }

    public Long getSampleSize() {
        return sampleSize;
    }

    public HistoricUserMetric setSampleSize(Long sampleSize) {
        this.sampleSize = sampleSize;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public HistoricUserMetric setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public HistoricUserMetric setCollectorId(String collectorId) {
        this.collectorId = collectorId;
        return this;
    }

    public ChronoUnit getHistoricType() {
        return historicType;
    }

    public HistoricUserMetric setHistoricType(ChronoUnit historicType) {
        this.historicType = historicType;
        return this;
    }

    @Override
    public Object getId() {
        return this.id;
    }

    public HistoricUserMetric setId(String id) {
        this.id = id;
        return this;
    }
}
