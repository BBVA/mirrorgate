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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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
import com.bbva.arq.devops.ae.mirrorgate.support.TestObjectFactory;
import com.bbva.arq.devops.ae.mirrorgate.support.TestUtil;
import com.bbva.arq.devops.ae.mirrorgate.support.Platform;
import java.util.ArrayList;
import java.util.Arrays;
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

    private MockMvc mockMvc = null;

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
        DashboardDTO dashboard = TestObjectFactory.createDashboard();

        String appName1 = "mirrorgateApp";
        String appName2 = "mirrorgateApp2";
        List<String> appsNames = new ArrayList<>();
        appsNames.add(appName1);
        appsNames.add(appName2);

        ApplicationDTO app1 = new ApplicationDTO()
                .setAppname(appName1)
                .setRatingTotal(1003)
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
                ));
        ApplicationDTO app2 = new ApplicationDTO()
                .setAppname(appName2)
                .setRatingTotal(1203)
                .setPlatform(Platform.IOS)
                .setReviews(Arrays.asList(
                        new ReviewDTO()
                                .setAuthor("reviewer2")
                                .setRate(4.5)
                                .setTimestamp(2L)
                                .setComment("comment")
                ));
        List<ApplicationDTO> apps = new ArrayList<>();
        apps.add(app1);
        apps.add(app2);

        when(dashboardService.getApplicationsByDashboardName(dashboard.getName())).thenReturn(appsNames);
        when(dashboardService.getDashboard(dashboard.getName())).thenReturn(dashboard);
        when(reviewService.getAverageRateByAppNames(appsNames, dashboard.getMarketsStatsDays())).thenReturn(apps);

        this.mockMvc.perform(get("/dashboards/" + dashboard.getName() + "/applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appname", equalTo(app1.getAppname())))
                .andExpect(jsonPath("$[0].ratingTotal", equalTo((int) app1.getRatingTotal())))
                .andExpect(jsonPath("$[0].platform", equalTo(app1.getPlatform().toString())))
                .andExpect(jsonPath("$[0].reviews[0].author", equalTo(app1.getReviews().get(0).getAuthor())))
                .andExpect(jsonPath("$[0].reviews[0].rate", equalTo(app1.getReviews().get(0).getRate())))
                .andExpect(jsonPath("$[0].reviews[0].timestamp", equalTo((int) app1.getReviews().get(0).getTimestamp())))
                .andExpect(jsonPath("$[0].reviews[0].comment", equalTo(app1.getReviews().get(0).getComment())))
                .andExpect(jsonPath("$[0].reviews[1].author", equalTo(app1.getReviews().get(1).getAuthor())))
                .andExpect(jsonPath("$[0].reviews[1].rate", equalTo(app1.getReviews().get(1).getRate())))
                .andExpect(jsonPath("$[0].reviews[1].timestamp", equalTo((int) app1.getReviews().get(1).getTimestamp())))
                .andExpect(jsonPath("$[0].reviews[1].comment", equalTo(app1.getReviews().get(1).getComment())))
                .andExpect(jsonPath("$[1].ratingTotal", equalTo((int) app2.getRatingTotal())))
                .andExpect(jsonPath("$[1].platform", equalTo(app2.getPlatform().toString())))
                .andExpect(jsonPath("$[1].reviews[0].author", equalTo(app2.getReviews().get(0).getAuthor())))
                .andExpect(jsonPath("$[1].reviews[0].rate", equalTo(app2.getReviews().get(0).getRate())))
                .andExpect(jsonPath("$[1].reviews[0].comment", equalTo(app2.getReviews().get(0).getComment())))
                .andExpect(jsonPath("$[1].reviews[0].timestamp", equalTo((int) app2.getReviews().get(0).getTimestamp())));
    }

    @Test
    public void createReviewTest() throws Exception {
        Review review1 = createReview();
        Review review2 = createReview();

        List<Review> list = new ArrayList<>();
        list.add(review1);
        list.add(review2);

        Iterable<Review> reviews = list;

        List<String> ids = new ArrayList();
        ids.add(review1.getId().toString());
        ids.add(review2.getId().toString());

        when(reviewService.save(reviews)).thenReturn(ids);

        this.mockMvc.perform(post("/api/reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reviews)))
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void createReviewFailTest() throws Exception {
        Review review1 = createReview();
        Review review2 = createReview();

        List<Review> list = new ArrayList<>();
        list.add(review1);
        list.add(review2);

        Iterable<Review> reviews = list;

        when(reviewService.save(any())).thenThrow(ReviewsConflictException.class);

        this.mockMvc.perform(post("/api/reviews")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reviews)))
                .andExpect(status().is(HttpStatus.CONFLICT.value()));
    }

    @Test
    public void createFeedbackReviewTest() throws Exception {
        Review review = createFeedbackReview();

        when(reviewService.saveApplicationReview(eq(review.getAppname()), any())).thenReturn(map(review));

        MockHttpServletRequestBuilder mockHSRB = post("/reviews/" + review.getAppname());

        this.mockMvc.perform(mockHSRB
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("rate", String.valueOf(review.getStarrating()))
            .param("comment", review.getComment()))
            .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void createFeedbackReviewBadRequestTest() throws Exception {
        Review review = createFeedbackReview();

        when(reviewService.saveApplicationReview(eq(review.getAppname()), any())).thenReturn(map(review));

        MockHttpServletRequestBuilder mockHSRB = post("/reviews/" + review.getAppname());

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
        Review review = createFeedbackReview();

        when(reviewService.saveApplicationReview(eq(review.getAppname()), any())).thenReturn(map(review));

        MockHttpServletRequestBuilder mockHSRB = post("/reviews/" + review.getAppname());

        this.mockMvc.perform(mockHSRB
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("rate", String.valueOf(review.getStarrating()))
            .param("comment", review.getComment())
            .param("url", "foobar"))
            .andExpect(status().is(HttpStatus.FOUND.value()));
    }

    private Review createReview() {
        Review review = new Review();
        review.setId(ObjectId.get());
        review.setAppname("MirrorGateApp");
        review.setAuthorName("Author");
        review.setComment("Good App!");
        review.setPlatform(Platform.Android);
        review.setStarrating(4.0);

        return review;
    }

    private Review createFeedbackReview() {
        Review review = new Review();
        review.setId(ObjectId.get());
        review.setAppname("Foobar");
        review.setAuthorName("Author");
        review.setComment("Good App!");
        review.setStarrating(5.0);

        return review;
    }

}

