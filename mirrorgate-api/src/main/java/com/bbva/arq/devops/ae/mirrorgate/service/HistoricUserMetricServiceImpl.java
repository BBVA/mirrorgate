package com.bbva.arq.devops.ae.mirrorgate.service;

import static com.bbva.arq.devops.ae.mirrorgate.mapper.UserMetricMapper.mapToHistoric;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import com.bbva.arq.devops.ae.mirrorgate.repository.HistoricUserMetricRepository;
import com.bbva.arq.devops.ae.mirrorgate.utils.LocalDateTimeHelper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class HistoricUserMetricServiceImpl implements HistoricUserMetricService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoricUserMetricServiceImpl.class);
    private static final int MAX_NUMBER_OF_DAYS_TO_STORE = 30;

    private HistoricUserMetricRepository repository;

    @Autowired
    public HistoricUserMetricServiceImpl(HistoricUserMetricRepository repository){

        this.repository = repository;
    }


    @Override
    public HistoricUserMetric createNextPeriod(UserMetric userMetric) {

        HistoricUserMetric historicUserMetric = mapToHistoric(userMetric);

        historicUserMetric.setSampleSize(0d);
        historicUserMetric.setTimestamp(LocalDateTimeHelper.getTimestampPeriod(userMetric.getTimestamp()));

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
            .filter( u -> "requestsNumber".compareTo(u.getName()) == 0)
            .collect(Collectors.toList());

        LOGGER.info("requestNumber user metrics: {}", requestNumberMetrics.size());

        requestNumberMetrics.forEach( r -> {
            HistoricUserMetric metric = getHistoricMetricForPeriod(LocalDateTimeHelper.getTimestampPeriod(r.getTimestamp()), r.getId());

            if (metric == null){
                metric = createNextPeriod(r);
            }

            metric.setSampleSize(metric.getSampleSize() + r.getSampleSize());
            repository.save(metric);
            removeExtraPeriodsForMetricAndIdentifier(MAX_NUMBER_OF_DAYS_TO_STORE, metric.getName(), metric.getIdentifier());
        });
    }

    @Override
    public void removeExtraPeriodsForMetricAndIdentifier(int daysToKeep, String metricName, String identifier) {

        List<HistoricUserMetric> oldPeriods =
            repository.findByNameAndIdentifierAndTimestampLessThan(metricName, identifier, LocalDateTimeHelper.getTimestampForNDaysAgo(daysToKeep));

        repository.delete(oldPeriods);
    }

}
