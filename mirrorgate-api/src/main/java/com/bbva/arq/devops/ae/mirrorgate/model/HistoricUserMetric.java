package com.bbva.arq.devops.ae.mirrorgate.model;

import java.time.temporal.ChronoUnit;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "historic_user_metrics")
public class HistoricUserMetric extends BaseModel{

    @Indexed
    private String identifier;

    private String viewId;

    private String appVersion;

    private String platform;

    @Indexed
    private String name;

    private Double value;

    private Double sampleSize;

    @Indexed
    private Long timestamp;

    /* Associated collector ID */
    private String collectorId;

    @Indexed
    private ChronoUnit historicType;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(Double sampleSize) {
        this.sampleSize = sampleSize;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
    }

    public ChronoUnit getHistoricType() {
        return historicType;
    }

    public void setHistoricType(ChronoUnit historicType) {
        this.historicType = historicType;
    }
}
