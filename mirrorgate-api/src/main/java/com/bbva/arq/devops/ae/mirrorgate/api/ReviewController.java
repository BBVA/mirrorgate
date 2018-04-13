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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.bbva.arq.devops.ae.mirrorgate.dto.ReviewDTO;
import com.bbva.arq.devops.ae.mirrorgate.exception.MirrorGateException;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.ReviewService;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Defines reviews rest methods
 */
@RestController
public class ReviewController {

    private static final Logger LOG = Logger.getLogger(ReviewController.class.getName());

    private final DashboardService dashboardService;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(DashboardService dashboardService, ReviewService reviewService) {
        this.dashboardService = dashboardService;
        this.reviewService = reviewService;
    }

    @RequestMapping(value = "/dashboards/{name}/applications", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getApplicationReviewRatings(@PathVariable("name") String name) {
        List<String> appNames = dashboardService.getApplicationsByDashboardName(name);
        return ResponseEntity.ok(
            reviewService.getAverageRateByAppNames(
                appNames, dashboardService.getDashboard(name).getMarketsStatsDays()
            )
        );
    }

    @RequestMapping(value = "/api/reviews",
            method = POST,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReviews(@Valid @RequestBody Iterable<Review> reviews) throws MirrorGateException {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.save(reviews));
    }

    @RequestMapping(value = "/reviews/{appid}", method = POST)
    public ResponseEntity<?> createReviewsOfApplication(
        HttpServletRequest request,
        @PathVariable("appid") String appId,
        @ModelAttribute() @Valid ReviewDTO review,
        @RequestParam(name = "url", required = false) String url
    ) throws MirrorGateException {
        String referer = request.getHeader(HttpHeaders.REFERER);
        LOG.log(Level.INFO, "Review -> Referer header value {0}", referer);

        ReviewDTO savedReview = reviewService.saveApplicationReview(appId, review);

        if(url == null) {
            if (savedReview == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
            }
        }else{
            URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();
            return ResponseEntity.status(HttpStatus.FOUND).location(uri).body(savedReview);
        }
    }
}
