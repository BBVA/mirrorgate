package com.bbva.arq.devops.ae.mirrorgate.repository;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface HistoricUserMetricRepository extends CrudRepository<HistoricUserMetric, ObjectId>, HistoricUserMetricRepositoryCustom {

    HistoricUserMetric findByTimestampAndIdentifierAndHistoricType(Long timestamp, String identifier, ChronoUnit unit);

    List<HistoricUserMetric> findByNameAndIdentifierAndHistoricTypeAndTimestampLessThan(String name, String identifier, ChronoUnit unit, long timestamp);

    List<HistoricUserMetric> findAllByViewIdInAndHistoricTypeAndNameInAndTimestampGreaterThanEqual(List<String> ids, ChronoUnit unit, List<String> names, long timestamp);
}
