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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationDTO;
import com.bbva.arq.devops.ae.mirrorgate.exception.ReviewsConflictException;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final long DAY_IN_MS = (long) 1000 * 60 * 60 * 24;

    @Autowired
    private ReviewRepository repository;

    @Override
    public List<ApplicationDTO> getAverageRateByAppNames(List<String> names) {
        List<ApplicationDTO> result = repository.getAverageRateByAppNames(names);

        Date sevenDaysBefore = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));

        List<ApplicationDTO> stats = repository.getAverageRateByAppNamesAfterTimestamp(names, sevenDaysBefore.getTime());

        result.forEach((app) -> {
            Optional<ApplicationDTO> appStats = stats.stream()
                    .filter((stat) -> stat.getAppname().equals(app.getAppname()) && stat.getPlatform().equals(app.getPlatform()))
                    .findFirst();
            if(appStats.isPresent()) {
                app.setRating7Days(appStats.get().getRating7Days());
                app.setVotes7Days(appStats.get().getVotes7Days());
            }
        });

        return result;
    }

    private List<String> getReviewIds(Iterable<Review> reviews) {
        List<String> savedIDs = new ArrayList<>();

        reviews.forEach(request -> savedIDs.add(request.getId().toString()));

        return savedIDs;
    }

    @Override
    public List<String> create(Iterable<Review> reviews) {
        Iterable<Review> newReviews = repository.save(reviews);

        if (newReviews == null) {
            throw new ReviewsConflictException("Save reviews error");
        }

        return getReviewIds(reviews);
    }
}