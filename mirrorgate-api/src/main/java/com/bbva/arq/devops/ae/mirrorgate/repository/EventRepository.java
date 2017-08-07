package com.bbva.arq.devops.ae.mirrorgate.repository;

import com.bbva.arq.devops.ae.mirrorgate.model.Event;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, ObjectId>{

    Event findFirstByOrderByTimestampDesc();

    List<Event> findByTimestampGreaterThanOrderByTimestampAsc(Long timestamp);
}
