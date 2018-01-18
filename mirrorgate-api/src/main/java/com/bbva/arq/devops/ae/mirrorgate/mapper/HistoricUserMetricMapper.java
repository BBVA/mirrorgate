package com.bbva.arq.devops.ae.mirrorgate.mapper;

import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricUserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;

public class HistoricUserMetricMapper {

    public static HistoricUserMetric mapToHistoric(UserMetric origin) {
        return new HistoricUserMetric()
                .setSampleSize(origin.getSampleSize())
                .setAppVersion(origin.getAppVersion())
                .setCollectorId(origin.getCollectorId())
                .setIdentifier(origin.getId())
                .setName(origin.getName())
                .setPlatform(origin.getPlatform())
                .setValue(origin.getValue())
                .setViewId(origin.getViewId())
                .setTimestamp(origin.getTimestamp());
    }

    public static HistoricUserMetricDTO map(HistoricUserMetric source) {
        return new HistoricUserMetricDTO()
                .setSampleSize(source.getSampleSize())
                .setAppVersion(source.getAppVersion())
                .setCollectorId(source.getCollectorId())
                .setName(source.getName())
                .setPlatform(source.getPlatform())
                .setValue(source.getValue())
                .setViewId(source.getViewId())
                .setTimestamp(source.getTimestamp());
    }
}
