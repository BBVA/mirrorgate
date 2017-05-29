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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationReviewsDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
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

    @Before
    public void init(){

        Review review = createReview(Platform.IOS, "mirrorgate", "123456", "comment 1",1);
        Review review2 = createReview(Platform.IOS, "mirrorgate", "123457", "comment 2",2);

        reviewRepository.save(review);
        reviewRepository.save(review2);
    }

    @Test
    public void testAggregationNoResults(){

        List<String> appNamesList = Arrays.asList("mood");
        List<ApplicationReviewsDTO> reviews = reviewRepository.getLastReviewPerApplication(appNamesList);

        assertTrue(reviews.size() == 0);
    }

    @Test
    public void testAggregationNoApps(){

        List<String> appNamesList = new ArrayList<>();
        List<ApplicationReviewsDTO> reviews = reviewRepository.getLastReviewPerApplication(appNamesList);

        assertTrue(reviews.size() == 0);
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


    private Review createReview(Platform platform, String appName, String commentId, String comment, int timestamp) {

        Review review = new Review();

        review.setTimestamp(timestamp);
        review.setAppname(appName);
        review.setComment(comment);
        review.setPlatform(platform);
        review.setCommentId(commentId);

        review.setAuthorName("Author");
        review.setStarrating(3.2);

        return review;
    }

}
