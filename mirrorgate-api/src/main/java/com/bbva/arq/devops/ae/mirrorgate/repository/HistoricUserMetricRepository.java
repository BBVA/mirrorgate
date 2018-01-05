package com.bbva.arq.devops.ae.mirrorgate.repository;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface HistoricUserMetricRepository extends CrudRepository<HistoricUserMetric, ObjectId> {

    HistoricUserMetric findByTimestampAndIdentifierAndHistoricType(Long timestamp, String identifier, ChronoUnit unit);

    List<HistoricUserMetric> findByNameAndIdentifierAndHistoricTypeAndTimestampLessThan(String name, String identifier, ChronoUnit unit, long timestamp);

    List<HistoricUserMetric> findAllByViewIdInAndValueGreaterThanAndNameAndHistoricTypeOrderByTimestampAsc(Pageable page, List<String> ids, double value, String name, ChronoUnit historicType);

    List<HistoricUserMetric> findAllByViewIdInAndHistoricTypeAndTimestampGreaterThanEqual(List<String> ids, ChronoUnit unit, long timestamp);
}
