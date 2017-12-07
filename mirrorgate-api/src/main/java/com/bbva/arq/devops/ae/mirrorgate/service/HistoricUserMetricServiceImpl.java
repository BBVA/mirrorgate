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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;



@Service
public class HistoricUserMetricServiceImpl implements HistoricUserMetricService {

    private static final int MAX_NUMBER_OF_PERIODS_TO_STORE = 30;
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
    public List<HistoricUserMetric> getLastNPeriods(int n, String metricName, String identifier) {

        return repository.findByNameAndIdentifierOrderByTimestampAsc(new PageRequest(0, n), metricName, identifier);
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
            removeExtraPeriodsForMetricAndIdentifier(MAX_NUMBER_OF_PERIODS_TO_STORE, metric.getName(), metric.getIdentifier());
        });

    }

    @Override
    public void removeExtraPeriodsForMetricAndIdentifier(int periodNumber, String metricName, String identifier) {

        List<HistoricUserMetric> lastNPeriods = getAllPeriodsForMetricAndIdentifier(metricName, identifier);

        int periodsToDelete = lastNPeriods.size() - periodNumber;

        for (int i = 0; i < periodsToDelete; i++){
            repository.delete(lastNPeriods.get(i));
        }
    }


    private long getUserMetricPeriod(UserMetric userMetric){

        Instant instant = Instant.ofEpochSecond(userMetric.getTimestamp());

        LocalDateTime metricTimestamp = LocalDateTime.ofInstant(instant,
                TimeZone.getTimeZone("UTC").toZoneId()).truncatedTo(ChronoUnit.HOURS);

        return metricTimestamp.toInstant(ZoneOffset.UTC).getEpochSecond();
    }


    private List<HistoricUserMetric> getAllPeriodsForMetricAndIdentifier(String metricName, String identifier) {

        return repository.findByNameAndIdentifierOrderByTimestampAsc(null, metricName, identifier);
    }


}
