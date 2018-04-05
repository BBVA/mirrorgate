package com.bbva.arq.devops.ae.mirrorgate.model;

import java.time.temporal.ChronoUnit;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "historic_user_metrics")
public class HistoricUserMetric extends BaseModel {

    @Indexed
    private String identifier;

    private String viewId;

    private String appVersion;

    private String platform;

    private String name;

    private Double value;

    private Long sampleSize;

    @Indexed
    private Long timestamp;

    /* Associated collector ID */
    private String collectorId;

    @Indexed
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
}
