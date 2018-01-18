package com.bbva.arq.devops.ae.mirrorgate.repository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.bbva.arq.devops.ae.mirrorgate.model.HistoricUserMetric;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

public class HistoricUserMetricRepositoryImpl implements HistoricUserMetricRepositoryCustom {

    private MongoTemplate mongoTemplate;

    @Autowired
    public HistoricUserMetricRepositoryImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }


    public static class HistoricUserMetricStats {

        @Id
        private String name;
        private Double value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }
    }

    @Override
    public List<HistoricUserMetricStats> getUserMetricAverageTendencyForPeriod(List<String> views, List<String> metricNames, long limitTimestamp) {

        TypedAggregation<HistoricUserMetric> aggregation = newAggregation(HistoricUserMetric.class,
            match(Criteria.where("viewId").in(views)
                .and("name").in(metricNames)
                .and("value").gte(0d)
                .and("historicType").is(ChronoUnit.HOURS)
                .and("timestamp").gte(limitTimestamp)),
            group("name").avg("value").as("value"),
            project("name","value")
        );

        AggregationResults<HistoricUserMetricStats> groupResults =
            mongoTemplate.aggregate(aggregation,"historic_user_metrics", HistoricUserMetricStats.class);

        return groupResults.getMappedResults();
    }
}
