/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.repository;

import com.bbva.arq.devops.ae.mirrorgate.model.UserMetric;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UserMetricsRepositoryImplTest {

    @Autowired
    private UserMetricsRepository userMetricsRepository;

    @Before
    public void init(){
        userMetricsRepository.save(createUserMetric("AWS/123456789012/alb/alb1", "AWS", "requestsNumber", 2500D));
        userMetricsRepository.save(createUserMetric("AWS/123456789012/alb/alb2", "AWS", "errorsNumber", 50D));
        userMetricsRepository.save(createUserMetric("AWS/123456789012/apigateway/restapi", "AWS", "errorsNumber", 10D));
        userMetricsRepository.save(createUserMetric("AWS/111111111111/elb/elb1", "AWS", "requestsNumber", 10000D));
        userMetricsRepository.save(createUserMetric("GCP/222222222222/elb/elb1", "GCP", "requestsNumber", 20000D));
    }

    @After
    public void clean(){
        userMetricsRepository.deleteAll();
    }

    @Test
    public void findUserMetricsByViewIdsWithoutResultsTest(){
        List<String> viewIds = Collections.singletonList("aaa");

        List<UserMetric> userMetrics = userMetricsRepository.findAllStartingWithViewId(viewIds);

        assertEquals(Collections.emptyList(), userMetrics);
    }

    @Test
    public void findUserMetricsByViewIdsTest(){
        List<String> viewIds1 = Collections.singletonList("AWS/123456789012");
        List<String> viewIds2 = Collections.singletonList("AWS/123456789012/alb");
        List<String> viewIds3 = Collections.singletonList("AWS/123456789012/apigateway/restapi");

        List<UserMetric> userMetrics1 = userMetricsRepository.findAllStartingWithViewId(viewIds1);
        List<UserMetric> userMetrics2 = userMetricsRepository.findAllStartingWithViewId(viewIds2);
        List<UserMetric> userMetrics3 = userMetricsRepository.findAllStartingWithViewId(viewIds3);

        assertEquals(3, userMetrics1.size());
        assertEquals(2, userMetrics2.size());
        assertEquals(1, userMetrics3.size());

        assertTrue(userMetrics1.stream().map(UserMetric::getViewId).collect(Collectors.toList()).contains("AWS/123456789012/alb/alb1"));
        assertTrue(userMetrics1.stream().map(UserMetric::getViewId).collect(Collectors.toList()).contains("AWS/123456789012/alb/alb2"));
        assertTrue(userMetrics1.stream().map(UserMetric::getViewId).collect(Collectors.toList()).contains("AWS/123456789012/apigateway/restapi"));

        assertTrue(userMetrics2.stream().map(UserMetric::getViewId).collect(Collectors.toList()).contains("AWS/123456789012/alb/alb1"));
        assertTrue(userMetrics2.stream().map(UserMetric::getViewId).collect(Collectors.toList()).contains("AWS/123456789012/alb/alb2"));

        assertTrue(userMetrics3.stream().map(UserMetric::getViewId).collect(Collectors.toList()).contains("AWS/123456789012/apigateway/restapi"));
    }

    @Test
    public void findUserMetricsByMultipleViewIdsTest(){
        List<String> viewIds = Arrays.asList("AWS/111111111111", "GCP/222222222222");

        List<UserMetric> userMetrics = userMetricsRepository.findAllStartingWithViewId(viewIds);

        assertEquals(2, userMetrics.size());

        assertTrue(userMetrics.stream().map(UserMetric::getViewId).collect(Collectors.toList()).contains("AWS/111111111111/elb/elb1"));
        assertTrue(userMetrics.stream().map(UserMetric::getViewId).collect(Collectors.toList()).contains("GCP/222222222222/elb/elb1"));
    }

    private UserMetric createUserMetric(String viewId, String platform, String name, Double value){
        UserMetric userMetric = new UserMetric();

        userMetric.setViewId(viewId);
        userMetric.setPlatform(platform);
        userMetric.setName(name);
        userMetric.setValue(value);

        return userMetric;
    }

}
