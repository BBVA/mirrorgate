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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.repository.ReviewRepository;
import com.bbva.arq.devops.ae.mirrorgate.utils.MirrorGateException;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks private ReviewServiceImpl reviewService;

    @Test
    public void getAverageRateByAppNamesTest() {

        String appName1 = "mirrorgateApp";
        String appName2 = "mirrorgateApp2";
        List<String> appsNames = new ArrayList<>();
        appsNames.add(appName1);
        appsNames.add(appName2);

        ApplicationDTO app1 = new ApplicationDTO(appName1, 3, Platform.Android, "reviewer1", 2.0, 1, "comment");
        ApplicationDTO app2 = new ApplicationDTO(appName2, 4.5, Platform.IOS, "reviewer2", 4.5, 2, "comment");

        List<ApplicationDTO> apps = new ArrayList<>();
        apps.add(app1);
        apps.add(app2);

        when(reviewRepository.getAverageRateByAppNames(appsNames))
                .thenReturn(apps);

        List<ApplicationDTO> appsByNames
                =                reviewService.getAverageRateByAppNames(appsNames);
        verify(reviewRepository, times(1))
            .getAverageRateByAppNames(appsNames);

        assertThat(appsByNames.get(0)).isEqualTo(app1);
        assertThat(appsByNames.get(1)).isEqualTo(app2);
    }


    @Test
    public void createReviewTest() throws MirrorGateException {
        Review review1 = createReview();
        Review review2 = createReview();

        List<Review> list = new ArrayList<>();
        list.add(review1);
        list.add(review2);

        Iterable<Review> reviews = list;

        when(reviewRepository.save(reviews))
                .thenReturn(reviews);

        List<String> reviewId = reviewService.create(reviews);
        verify(reviewRepository, times(1)).save(reviews);

        assertThat(reviewId.get(0)).isEqualTo(review1.getId().toString());
        assertThat(reviewId.get(1)).isEqualTo(review2.getId().toString());

    }

    @Test(expected = MirrorGateException.class)
    public void createReviewThrowMirrorGateErrorTest() throws MirrorGateException {
        Review review1 = createReview();
        Review review2 = createReview();

        List<Review> list = new ArrayList<>();
        list.add(review1);
        list.add(review2);

        Iterable<Review> reviews = list;

        when(reviewRepository.save(reviews)).thenReturn(null);

        reviewService.create(reviews);
    }

    private Review createReview() {
        Review review = new Review();
        review.setId(ObjectId.get());
        review.setAppname("MirrorGateApp");
        review.setAuthorName("Author");
        review.setComment("Good App!");
        review.setPlatform(Platform.Android);
        review.setStarrating(4);

        return review;
    }
}
