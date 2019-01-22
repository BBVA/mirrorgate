/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.ApplicationDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.ReviewDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.repository.ReviewRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.Platform;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bbva.arq.devops.ae.mirrorgate.mapper.ReviewMapper.map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ReviewServiceTests {

    private static final long DAY_IN_MS = (long) 1000 * 60 * 60 * 24;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    public void getAverageRateByAppNamesTest() {

        String appName1 = "mirrorgateApp";
        List<String> appsNames = new ArrayList<>();
        appsNames.add(appName1);

        ApplicationDTO app1 = new ApplicationDTO()
            .setAppname(appName1)
            .setRatingTotal(1003)
            .setPlatform(Platform.Android)
            .setVotesShortTerm(8)
            .setRatingShortTerm(19)
            .setVotesLongTerm(61)
            .setRatingLongTerm(195)
            .setReviews(Arrays.asList(
                new ReviewDTO()
                    .setAuthor("reviewer1")
                    .setRate(2.0)
                    .setTimestamp(1L)
                    .setComment("comment"),
                new ReviewDTO()
                    .setAuthor("reviewer3")
                    .setRate(3.0)
                    .setTimestamp(2L)
                    .setComment("comment2")
            ));
        List<ApplicationDTO> apps = new ArrayList<>();
        apps.add(app1);

        when(reviewRepository.getAppInfoByAppNames(appsNames)).thenReturn(apps);
        when(reviewRepository.getAverageRateByAppNamesAfterTimestamp(eq(appsNames), any())).then((Answer<List<ApplicationDTO>>) invocation -> {
            List<ApplicationDTO> stats = new ArrayList<>();
            ApplicationDTO app = new ApplicationDTO()
                .setAppname(app1.getAppname())
                .setPlatform(app1.getPlatform());

            if ((Long) invocation.getArgument(1) > System.currentTimeMillis() - 8 * DAY_IN_MS) {
                app.setVotesShortTerm(app1.getVotesShortTerm());
                app.setRatingShortTerm(app1.getRatingShortTerm());
                stats.add(app);
            } else {
                app.setVotesShortTerm(app1.getVotesLongTerm());
                app.setRatingShortTerm(app1.getRatingLongTerm());
                stats.add(app);
            }

            return stats;
        });

        List<ApplicationDTO> appsByNames
            = reviewService.getAverageRateByAppNames(appsNames, 7);

        assertThat(appsByNames.get(0)).isEqualTo(app1);
    }

    @Test
    public void createReviewTest() {
        Review review1 = createReview().setCommentId("123354");
        Review review2 = createReview().setCommentId("24324324");

        Iterable<Review> reviews = Arrays.asList(review1, review2);

        when(reviewRepository.saveAll(reviews)).thenReturn(reviews);

        List<String> reviewIds = reviewService.saveAll(reviews);

        assertThat(review1.getCommentId()).isEqualTo(reviewIds.get(0));
        assertThat(review2.getCommentId()).isEqualTo(reviewIds.get(1));
    }

    @Test
    public void createFeedbackReviewTest() {
        Review review = createFeedbackReview();

        ReviewDTO savedReview = map(review);

        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewDTO reviewDTO = reviewService.saveApplicationReview(review.getAppname(), savedReview);

        assertThat(reviewDTO.getComment()).isEqualTo(savedReview.getComment());
        assertThat(reviewDTO.getRate()).isEqualTo(savedReview.getRate());
        assertThat(reviewDTO.getTimestamp()).isEqualTo(savedReview.getTimestamp());
    }

    private Review createReview() {
        Review review = new Review();
        review.setId(ObjectId.get());
        return review
            .setAppname("MirrorGateApp")
            .setAuthorName("Author")
            .setComment("Good App!")
            .setPlatform(Platform.Android)
            .setStarrating(4)
            .setTimestamp(1L);
    }

    private Review createFeedbackReview() {
        Review review = new Review();
        review.setId(ObjectId.get());
        return review.setAppname("Foobar")
            .setAuthorName("Author")
            .setComment("Good App!")
            .setStarrating(5.0)
            .setTimestamp(1L);
    }
}
