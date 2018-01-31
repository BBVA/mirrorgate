package com.bbva.arq.devops.ae.mirrorgate.model;

import java.time.temporal.ChronoUnit;
import org.springframework.data.annotation.Id;

public class HistoricUserMetricStats {

    @Id
    private String name;

    private String viewId;

    private String appVersion;

    private String platform;

    private Double value;

    private Double sampleSize;

    private String collectorId;

    private ChronoUnit historicType;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
