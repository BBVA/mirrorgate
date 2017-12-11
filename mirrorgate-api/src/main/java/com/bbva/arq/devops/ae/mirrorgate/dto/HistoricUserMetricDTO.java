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
}
