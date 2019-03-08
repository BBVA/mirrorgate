package com.bbva.arq.devops.ae.mirrorgate.mapper;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;

import java.time.temporal.ChronoUnit;

public class HistoricUserMetricMapper {

    public static HistoricUserMetric mapToHistoric(UserMetric origin, ChronoUnit unit, long periodTimestamp) {
        return new HistoricUserMetric()
            .setId(generateId(origin.getIdentifier(), unit, periodTimestamp))
            .setIdentifier(origin.getIdentifier())
            .setHistoricType(unit)
            .setTimestamp(periodTimestamp)
            .setSampleSize(origin.getSampleSize())
            .setAppVersion(origin.getAppVersion())
            .setCollectorId(origin.getCollectorId())
            .setName(origin.getName())
            .setPlatform(origin.getPlatform())
            .setValue(origin.getValue())
            .setViewId(origin.getViewId());
    }

    public static String generateId(String identifier, ChronoUnit type, long periodTimestamp) {
        return identifier + type + periodTimestamp;
    }
}
