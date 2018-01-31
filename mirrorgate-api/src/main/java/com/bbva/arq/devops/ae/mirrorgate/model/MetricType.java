package com.bbva.arq.devops.ae.mirrorgate.model;

import java.util.Arrays;
import java.util.List;

public enum MetricType {

    USER_METRICS,
    OPERATION_METRICS;

    private List<String> metricNames;

    static{
        OPERATION_METRICS.metricNames = Arrays.asList("availabilityRate","errorsNumber", "infrastructureCost", "requestsNumber", "responseTime");
        USER_METRICS.metricNames = Arrays.asList("7dayUsers","activeUsers");
    }

    public List<String> getMetricNames(){
        return metricNames;
    }

}
