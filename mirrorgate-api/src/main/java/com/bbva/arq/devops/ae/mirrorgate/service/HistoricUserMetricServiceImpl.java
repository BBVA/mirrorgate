package com.bbva.arq.devops.ae.mirrorgate.service;

import static com.bbva.arq.devops.ae.mirrorgate.mapper.UserMetricMapper.mapToHistoric;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class HistoricUserMetricServiceImpl implements HistoricUserMetricService {

    private HistoricUserMetricRepository repository;

    @Autowired
    public HistoricUserMetricServiceImpl(HistoricUserMetricRepository repository){

        this.repository = repository;
    }


    @Override
    public HistoricUserMetric createNextPeriod(UserMetric userMetric) {

        HistoricUserMetric historicUserMetric = mapToHistoric(userMetric);

        historicUserMetric.setSampleSize(0d);
        historicUserMetric.setTimestamp(getUserMetricPeriod(userMetric));

        return historicUserMetric;
    }

    @Override
    public HistoricUserMetric getHistoricMetricForPeriod(long periodTimestamp, String identifier) {

        return repository.findByTimestampAndIdentifier(periodTimestamp, identifier);
    }

    @Override
    public void getLastNPeriods(int n) {

        //Recover from DB ordered
    }

    @Override
    public void addToCurrentPeriod(Iterable<UserMetric> saved) {

        List<UserMetric> requestNumberMetrics = StreamSupport.stream(saved.spliterator(), false)
            .filter( u -> "requestNumber".compareTo(u.getName()) == 0)
            .collect(Collectors.toList());


        requestNumberMetrics.forEach( r -> {
            HistoricUserMetric metric = getHistoricMetricForPeriod(getUserMetricPeriod(r), r.getId());

            if (metric == null){
                metric = createNextPeriod(r);
            }

            metric.setSampleSize(metric.getSampleSize() + r.getSampleSize());
            repository.save(metric);
        });

        removePeriod();
    }

    @Override
    public void removePeriod() {
        getLastNPeriods(31);
    }


    private long getUserMetricPeriod(UserMetric userMetric){

        Instant instant = Instant.ofEpochSecond(userMetric.getTimestamp());

        LocalDateTime metricTimestamp = LocalDateTime.ofInstant(instant,
                TimeZone.getTimeZone("UTC").toZoneId()).truncatedTo(ChronoUnit.HOURS);

        return metricTimestamp.toInstant(ZoneOffset.UTC).getEpochSecond();
    }

}
