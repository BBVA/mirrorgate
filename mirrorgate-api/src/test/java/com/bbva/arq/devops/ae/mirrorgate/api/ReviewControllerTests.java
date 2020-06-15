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

package com.bbva.arq.devops.ae.mirrorgate.api;

import static com.bbva.arq.devops.ae.mirrorgate.mapper.ReviewMapper.map;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bbva.arq.devops.ae.mirrorgate.dto.ApplicationDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.ReviewDTO;
import com.bbva.arq.devops.ae.mirrorgate.exception.ReviewsConflictException;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.ReviewService;
import com.bbva.arq.devops.ae.mirrorgate.support.Platform;
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import com.bbva.arq.devops.ae.mirrorgate.support.TestUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebMvcTest(ReviewController.class)
@WebAppConfiguration
public class ReviewControllerTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;
    @MockBean
    private ReviewService reviewService;
    @MockBean
    private DashboardService dashboardService;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getApplicationReviewRatingsTest() throws Exception {
        final DashboardDTO dashboard = TestObjectFactory.createDashboard();

        final String appName1 = "mirrorgateApp";
        final String appName2 = "mirrorgateApp2";
        final List<String> appsNames = Arrays.asList(appName1, appName2);

        final ApplicationDTO app1 = new ApplicationDTO()
            .setAppname(appName1)
            .setRatingTotal(1003)
            .setVotesTotal(2000L)
            .setPlatform(Platform.Android)
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
            ))
            .setUrl("http://fake.org")
            .setShortTermLength(7)
            .setLongTermLength(30);
        final ApplicationDTO app2 = new ApplicationDTO()
            .setAppname(appName2)
            .setRatingTotal(1203)
            .setVotesTotal(56000L)
            .setPlatform(Platform.IOS)
            .setReviews(Collections.singletonList(
                new ReviewDTO()
                    .setAuthor("reviewer2")
                    .setRate(4.5)
                    .setTimestamp(2L)
                    .setComment("comment")
            ))
            .setUrl("http://fake.org")
            .setShortTermLength(7)
            .setLongTermLength(30);

        when(dashboardService.getApplicationsByDashboardName(dashboard.getName())).thenReturn(appsNames);
        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);
        when(reviewService.getAverageRateByAppNames(appsNames, dashboard.getMarketsStatsDays()))
            .thenReturn(Arrays.asList(app1, app2));

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/applications"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].appname", equalTo(app1.getAppname())))
            .andExpect(jsonPath("$[0].ratingTotal", equalTo((int) app1.getRatingTotal())))
            .andExpect(jsonPath("$[0].votesTotal", equalTo((int) app1.getVotesTotal())))
            .andExpect(jsonPath("$[0].platform", equalTo(app1.getPlatform().getName())))
            .andExpect(jsonPath("$[0].url", equalTo(app1.getUrl())))
            .andExpect(jsonPath("$[0].shortTermLength", equalTo(app1.getShortTermLength())))
            .andExpect(jsonPath("$[0].longTermLength", equalTo(app1.getLongTermLength())))
            .andExpect(jsonPath("$[0].reviews[0].author", equalTo(app1.getReviews().get(0).getAuthor())))
            .andExpect(jsonPath("$[0].reviews[0].rate", equalTo(app1.getReviews().get(0).getRate())))
            .andExpect(jsonPath("$[0].reviews[0].timestamp", equalTo((int) app1.getReviews().get(0).getTimestamp())))
            .andExpect(jsonPath("$[0].reviews[0].comment", equalTo(app1.getReviews().get(0).getComment())))
            .andExpect(jsonPath("$[0].reviews[1].author", equalTo(app1.getReviews().get(1).getAuthor())))
            .andExpect(jsonPath("$[0].reviews[1].rate", equalTo(app1.getReviews().get(1).getRate())))
            .andExpect(jsonPath("$[0].reviews[1].timestamp", equalTo((int) app1.getReviews().get(1).getTimestamp())))
            .andExpect(jsonPath("$[0].reviews[1].comment", equalTo(app1.getReviews().get(1).getComment())))
            .andExpect(jsonPath("$[1].ratingTotal", equalTo((int) app2.getRatingTotal())))
            .andExpect(jsonPath("$[1].votesTotal", equalTo((int) app2.getVotesTotal())))
            .andExpect(jsonPath("$[1].platform", equalTo(app2.getPlatform().getName())))
            .andExpect(jsonPath("$[1].url", equalTo(app2.getUrl())))
            .andExpect(jsonPath("$[1].shortTermLength", equalTo(app2.getShortTermLength())))
            .andExpect(jsonPath("$[1].longTermLength", equalTo(app2.getLongTermLength())))
            .andExpect(jsonPath("$[1].reviews[0].author", equalTo(app2.getReviews().get(0).getAuthor())))
            .andExpect(jsonPath("$[1].reviews[0].rate", equalTo(app2.getReviews().get(0).getRate())))
            .andExpect(jsonPath("$[1].reviews[0].comment", equalTo(app2.getReviews().get(0).getComment())))
            .andExpect(jsonPath("$[1].reviews[0].timestamp", equalTo((int) app2.getReviews().get(0).getTimestamp())));
    }

    @Test
    public void createReviewTest() throws Exception {
        final Review review1 = createReview();
        final Review review2 = createReview();

        final List<Review> reviews = Arrays.asList(review1, review2);
        final List<String> ids = Arrays.asList(review1.getId().toString(), review2.getId().toString());

        when(reviewService.saveAll(reviews)).thenReturn(ids);

        this.mockMvc.perform(post("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviews)))
            .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void createReviewFailTest() throws Exception {
        final Review review1 = createReview();
        final Review review2 = createReview();

        final List<Review> reviews = Arrays.asList(review1, review2);

        when(reviewService.saveAll(any())).thenThrow(ReviewsConflictException.class);

        this.mockMvc.perform(post("/api/reviews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reviews)))
            .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

    @Test
    public void createFeedbackReviewTest() throws Exception {
        final Review review = createFeedbackReview();

        when(reviewService.saveApplicationReview(eq(review.getAppname()), any())).thenReturn(map(review));

        final MockHttpServletRequestBuilder mockHSRB = post("/reviews/" + review.getAppname());

        this.mockMvc.perform(mockHSRB
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("rate", String.valueOf(review.getStarrating()))
            .param("comment", review.getComment()))
            .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void createFeedbackReviewBadRequestTest() throws Exception {
        final Review review = createFeedbackReview();

        when(reviewService.saveApplicationReview(eq(review.getAppname()), any())).thenReturn(map(review));

        final MockHttpServletRequestBuilder mockHSRB = post("/reviews/" + review.getAppname());

        this.mockMvc.perform(mockHSRB
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("rate", "0")
            .param("comment", review.getComment()))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        this.mockMvc.perform(mockHSRB
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("rate", "10")
            .param("comment", review.getComment()))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        this.mockMvc.perform(mockHSRB
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("rate", "3"))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
        this.mockMvc.perform(mockHSRB
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("rate", "3")
            .param("comment", ""))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void createFeedbackReviewRedirectTest() throws Exception {
        final Review review = createFeedbackReview();

        when(reviewService.saveApplicationReview(eq(review.getAppname()), any())).thenReturn(map(review));

        final MockHttpServletRequestBuilder mockHSRB = post("/reviews/" + review.getAppname());

        this.mockMvc.perform(mockHSRB
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("rate", String.valueOf(review.getStarrating()))
            .param("comment", review.getComment())
            .param("url", "foobar"))
            .andExpect(status().is(HttpStatus.FOUND.value()));
    }

    private Review createReview() {
        final Review review = new Review();
        review.setId(ObjectId.get());
        review.setAppname("MirrorGateApp");
        review.setAuthorName("Author");
        review.setComment("Good App!");
        review.setPlatform(Platform.Android);
        review.setStarrating(4.0);

        return review;
    }

    private Review createFeedbackReview() {
        final Review review = new Review();
        review.setId(ObjectId.get());
        review.setAppname("Foobar");
        review.setAuthorName("Author");
        review.setComment("Good App!");
        review.setStarrating(5.0);

        return review;
    }
}
