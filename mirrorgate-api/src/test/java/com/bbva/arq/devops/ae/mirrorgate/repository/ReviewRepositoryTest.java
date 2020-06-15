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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.bbva.arq.devops.ae.mirrorgate.dto.ApplicationReviewsDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.support.Platform;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataMongoTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    private final Review review1 = TestObjectFactory.createReview(
        Platform.IOS,
        "mirrorgate",
        "123456",
        "comment 1",
        1,
        3.5,
        1
    );
    private final Review review2 = TestObjectFactory.createReview(
        Platform.IOS,
        "mirrorgate",
        "123457",
        "comment 2",
        2,
        5,
        10
    );
    private final Review review3 = TestObjectFactory.createReview(
        Platform.Android,
        "mirrorgate",
        "com.mirrorgate",
        "comment 1",
        3,
        3.5,
        1
    );
    private final Review review4 = TestObjectFactory.createReview(
        Platform.Android,
        "mirrorgate",
        "com.mirrorgate",
        "comment 2",
        4,
        5,
        10
    );

    @Before
    public void init() {
        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviewRepository.save(review4);
    }

    @After
    public void clean() {
        reviewRepository.deleteAll();
    }

    @Test
    public void testAggregationNoResults() {

        final List<String> appNamesList = Collections.singletonList("mood");
        final List<ApplicationReviewsDTO> reviews = reviewRepository.getLastReviewPerApplication(appNamesList);

        assertTrue(reviews.isEmpty());
    }

    @Test
    public void testAggregationNoApps() {

        final List<String> appNamesList = new ArrayList<>();
        final List<ApplicationReviewsDTO> reviews = reviewRepository.getLastReviewPerApplication(appNamesList);

        assertTrue(reviews.isEmpty());
    }

    @Test
    public void testRegularAggregation() {

        final List<String> appNamesList = Arrays.asList("mirrorgate", "mirrorgato", "mood");
        final List<ApplicationReviewsDTO> reviews = reviewRepository.getLastReviewPerApplication(appNamesList);

        assertEquals("mirrorgate", reviews.get(0).getAppId());
        assertEquals("mirrorgate", reviews.get(0).getAppName());
        assertEquals("123457", reviews.get(0).getCommentId());
        assertEquals(reviews.get(0).getPlatform(), Platform.IOS);
    }
}