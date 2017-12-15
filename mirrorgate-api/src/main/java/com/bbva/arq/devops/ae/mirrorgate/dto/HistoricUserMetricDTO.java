package com.bbva.arq.devops.ae.mirrorgate.dto;

public class HistoricUserMetricDTO {

    private String viewId;

    private String appVersion;

    private String platform;

    private String name;

    private Double value;

    private Double sampleSize;

    private Long timestamp;

    private String collectorId;


    public String getViewId() {
        return viewId;
    }

    public HistoricUserMetricDTO setViewId(String viewId) {
        this.viewId = viewId;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public HistoricUserMetricDTO setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getPlatform() {
        return platform;
    }

    public HistoricUserMetricDTO setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public String getName() {
        return name;
    }

    public HistoricUserMetricDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public HistoricUserMetricDTO setValue(Double value) {
        this.value = value;
        return this;
    }

    public Double getSampleSize() {
        return sampleSize;
    }

    public HistoricUserMetricDTO setSampleSize(Double sampleSize) {
        this.sampleSize = sampleSize;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public HistoricUserMetricDTO setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public HistoricUserMetricDTO setCollectorId(String collectorId) {
        this.collectorId = collectorId;
        return this;
    }
}
