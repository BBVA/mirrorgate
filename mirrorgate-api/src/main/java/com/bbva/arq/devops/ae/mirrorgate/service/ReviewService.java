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
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Service to handle Reviews
 */
public interface ReviewService {

    /**
     * Get the average of star rating of every application form a list of names.
     *
     * @param names A list with the name of the applications
     * @param marketsStatsDays Number of days for searching
     * @return Al List of ApplicationDTO objects with name and rate of every
     * application
     */
    List<ApplicationDTO> getAverageRateByAppNames(List<String> names, int marketsStatsDays);

    /**
     * Save reviews
     *
     * @param reviews List of reviews to save
     * @return List of Id of new Reviews
     */
    List<String> saveAll(Iterable<Review> reviews);

    ReviewDTO saveApplicationReview(String appId, ReviewDTO review);

    Iterable<Review> getReviewsByObjectId(List<ObjectId> objectIds);
}
