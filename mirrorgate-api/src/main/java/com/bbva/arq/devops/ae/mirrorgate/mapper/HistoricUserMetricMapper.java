package com.bbva.arq.devops.ae.mirrorgate.mapper;

import com.bbva.arq.devops.ae.mirrorgate.dto.HistoricUserMetricDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;

public class HistoricUserMetricMapper {

    public static HistoricUserMetric mapToHistoric(UserMetric origin){

        HistoricUserMetric historicUserMetric = new HistoricUserMetric();

        historicUserMetric.setSampleSize(origin.getSampleSize());
        historicUserMetric.setAppVersion(origin.getAppVersion());
        historicUserMetric.setCollectorId(origin.getCollectorId());
        historicUserMetric.setIdentifier(origin.getId());
        historicUserMetric.setName(origin.getName());
        historicUserMetric.setPlatform(origin.getPlatform());
        historicUserMetric.setValue(origin.getValue());
        historicUserMetric.setViewId(origin.getViewId());
        historicUserMetric.setTimestamp(origin.getTimestamp());

        return historicUserMetric;
    }

    public static HistoricUserMetricDTO map(HistoricUserMetric source){

        HistoricUserMetricDTO historicUserMetricDTO = new HistoricUserMetricDTO();

        historicUserMetricDTO.setSampleSize(source.getSampleSize());
        historicUserMetricDTO.setAppVersion(source.getAppVersion());
        historicUserMetricDTO.setCollectorId(source.getCollectorId());
        historicUserMetricDTO.setName(source.getName());
        historicUserMetricDTO.setPlatform(source.getPlatform());
        historicUserMetricDTO.setValue(source.getValue());
        historicUserMetricDTO.setViewId(source.getViewId());
        historicUserMetricDTO.setTimestamp(source.getTimestamp());

        return historicUserMetricDTO;

    }
}
