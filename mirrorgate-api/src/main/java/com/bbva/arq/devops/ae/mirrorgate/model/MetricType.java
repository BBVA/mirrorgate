package com.bbva.arq.devops.ae.mirrorgate.model;

import java.util.Arrays;
import java.util.List;

public enum MetricType {

    OVER_TIME,
    SNAPSHOT_METRICS;

    private List<String> metricNames;

    static{
        OVER_TIME.metricNames = Arrays.asList("availabilityRate", "errorsNumber", "requestsNumber", "responseTime");
        SNAPSHOT_METRICS.metricNames = Arrays.asList("7daysUsers", "activeUsers", "infrastructureCost");
    }

    public List<String> getMetricNames(){
        return metricNames;
    }

}
