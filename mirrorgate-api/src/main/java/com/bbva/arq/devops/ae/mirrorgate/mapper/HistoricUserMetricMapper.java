package com.bbva.arq.devops.ae.mirrorgate.mapper;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;

public class HistoricUserMetricMapper {

    public static HistoricUserMetric mapToHistoric(UserMetric origin) {
        return new HistoricUserMetric()
            .setIdentifier(origin.getId())
            .setSampleSize(origin.getSampleSize())
            .setAppVersion(origin.getAppVersion())
            .setCollectorId(origin.getCollectorId())
            .setName(origin.getName())
            .setPlatform(origin.getPlatform())
            .setValue(origin.getValue())
            .setViewId(origin.getViewId())
            .setTimestamp(origin.getTimestamp());
    }

}
