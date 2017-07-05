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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationReviewsDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.util.TestObjectFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    Review review = TestObjectFactory.createReview(
            Platform.IOS, "mirrorgate", "123456", "comment 1", 1, 3.5, 1);
    Review review2 = TestObjectFactory.createReview(
            Platform.IOS, "mirrorgate", "123457", "comment 2", 2, 5, 10);
    Review review3 = TestObjectFactory.createReview(
            Platform.Android, "mirrorgate", "com.mirrorgate", "comment 1", 3, 3.5, 1);
    Review review4 = TestObjectFactory.createReview(
            Platform.Android, "mirrorgate", "com.mirrorgate", "comment 2", 4, 5, 10);

    @Before
    public void init(){
        reviewRepository.save(review);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviewRepository.save(review4);
    }

    @Test
    public void testAggregationNoResults(){

        List<String> appNamesList = Arrays.asList("mood");
        List<ApplicationReviewsDTO> reviews = reviewRepository.getLastReviewPerApplication(appNamesList);

        assertTrue(reviews.isEmpty());
    }

    @Test
    public void testAggregationNoApps(){

        List<String> appNamesList = new ArrayList<>();
        List<ApplicationReviewsDTO> reviews = reviewRepository.getLastReviewPerApplication(appNamesList);

        assertTrue(reviews.isEmpty());
    }

    @Test
    public void testRegularAggregation(){

        List<String> appNamesList = Arrays.asList("mirrorgate", "mirrorgato", "mood");
        List<ApplicationReviewsDTO> reviews = reviewRepository.getLastReviewPerApplication(appNamesList);

        assertEquals("mirrorgate", reviews.get(0).getAppId());
        assertEquals("mirrorgate", reviews.get(0).getAppName());
        assertEquals("123457", reviews.get(0).getCommentId());
        assertTrue(reviews.get(0).getPlatform().equals(Platform.IOS));
    }

    @Test
    public void getAverageRateByAppNames() {
        List<String> names = Arrays.asList("mirrorgate", "mirrorgato", "mood");
        List<ApplicationDTO> applications = reviewRepository.getAverageRateByAppNames(names);

        List<Review> iosReviews = new ArrayList<>();
        iosReviews.add(review);
        iosReviews.add(review2);

        List<Review> androidReviews = new ArrayList<>();
        androidReviews.add(review3);
        androidReviews.add(review4);

        assertTrue(applications.get(0).getRate() == calculateAverage(iosReviews));
        assertTrue(applications.get(1).getRate() == calculateAverage(androidReviews));

    }

    private double calculateAverage(Review review, Review review2) {
        int total_accumulate = review.getAccumulate() + review2.getAccumulate();
        double total_starrating
                = review.getStarrating() * review.getAccumulate()
                + review2.getStarrating() * review2.getAccumulate();
        return total_starrating / total_accumulate;
    }

    private double calculateAverage(List<Review> reviews) {

        int total_accumulate = reviews.stream()
                .mapToInt(review -> review.getAccumulate()).sum();
        double total_starrating = reviews.stream()
                .mapToDouble(review -> review.getStarrating() * review.getAccumulate()).sum();

        return total_starrating / total_accumulate;
    }


}
