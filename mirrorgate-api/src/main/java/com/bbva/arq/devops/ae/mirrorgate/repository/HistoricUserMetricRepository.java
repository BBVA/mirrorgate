package com.bbva.arq.devops.ae.mirrorgate.repository;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface HistoricUserMetricRepository extends CrudRepository<HistoricUserMetric, ObjectId> {

    HistoricUserMetric findByTimestampAndIdentifier(Long timestamp, String identifier);

    List<HistoricUserMetric> findByNameAndIdentifierOrderByTimestampAsc(Pageable page, String name, String identifier);

    List<HistoricUserMetric> findByNameAndIdentifierAndTimestampLessThan(String name, String identifier,long timestamp);

    List<HistoricUserMetric> findAllByViewIdInAndValueGreaterThanAndNameOrderByTimestampAsc(Pageable page, List<String> ids, double value, String name);

}
