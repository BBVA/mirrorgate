package com.bbva.arq.devops.ae.mirrorgate.repository;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface HistoricUserMetricRepository extends CrudRepository<HistoricUserMetric, ObjectId> {

    HistoricUserMetric findByTimestampAndIdentifier(Long timestamp, String identifier);
}
