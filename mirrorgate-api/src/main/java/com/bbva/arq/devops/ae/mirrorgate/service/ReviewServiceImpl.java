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
import com.bbva.arq.devops.ae.mirrorgate.core.dto.ReviewDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;
import com.bbva.arq.devops.ae.mirrorgate.exception.ReviewsConflictException;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.repository.ReviewRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final long DAY_IN_MS = (long) 1000 * 60 * 60 * 24;
    private static final String MIRRORGATE = "$mirrorgate";
    private static final String MIRRORGATE_COMMENT_ID = MIRRORGATE + "_history";

    @Autowired
    private ReviewRepository repository;

    @Override
    public List<ApplicationDTO> getAverageRateByAppNames(List<String> names) {
        List<ApplicationDTO> result = repository.getAverageRateByAppNames(names);

        List<Review> history = repository.findHistoricalForApps(names);

        //Update merge the history review inside the result
        result.forEach((app) -> {
            Optional<Review> historyReviewOpt = history.stream()
                    .filter((r) -> app.getAppname().equals(r.getAppname()) && app.getPlatform() == r.getPlatform())
                    .findFirst();
            if(historyReviewOpt.isPresent()) {
                Review historyReview = historyReviewOpt.get();
                app.setVotesTotal(historyReview.getAmount());
                app.setRatingTotal((long) (historyReview.getStarrating() * historyReview.getAmount()));
            }
        });

        Date sevenDaysBefore = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
        Date monthBefore = new Date(System.currentTimeMillis() - (30 * DAY_IN_MS));

        List<ApplicationDTO> stats7Days = repository.getAverageRateByAppNamesAfterTimestamp(names, sevenDaysBefore.getTime());
        List<ApplicationDTO> statsMonth = repository.getAverageRateByAppNamesAfterTimestamp(names, monthBefore.getTime());

        result.forEach((app) -> {
            Optional<ApplicationDTO> appStats7Days = stats7Days.stream()
                    .filter((stat) -> stat.getAppname().equals(app.getAppname()) && stat.getPlatform().equals(app.getPlatform()))
                    .findFirst();
            Optional<ApplicationDTO> appStatsMonth = statsMonth.stream()
                    .filter((stat) -> stat.getAppname().equals(app.getAppname()) && stat.getPlatform().equals(app.getPlatform()))
                    .findFirst();

            if(appStats7Days.isPresent()) {
                app.setRating7Days(appStats7Days.get().getRating7Days());
                app.setVotes7Days(appStats7Days.get().getVotes7Days());
            }
            if(appStatsMonth.isPresent()) {
                //Ugly hack... we use the 7days to return the data even if it's for month :-(
                app.setRatingMonth(appStatsMonth.get().getRating7Days());
                app.setVotesMonth(appStatsMonth.get().getVotes7Days());
            }

        });

        return result;
    }

    private List<String> getReviewIds(Iterable<Review> reviews) {
        List<String> savedIDs = new ArrayList<>();

        reviews.forEach(request -> savedIDs.add(request.getCommentId()));

        return savedIDs;
    }

    @Override
    public List<String> save(Iterable<Review> reviews) {

        List<Review> singleReviews = StreamSupport.stream(reviews.spliterator(), false)
                .filter((r) -> r.getTimestamp() != null).collect(Collectors.toList());

        List<Review> existingReviews = repository
                .findAllByCommentIdIn(singleReviews.stream().map(Review::getCommentId).collect(Collectors.toList()));

        singleReviews = singleReviews.stream()
                .filter((r) -> {
                    //We exclude reviews that equal existing one and update the objectId for those
                    // different and already in the DB while keeping the new ones
                    for (Review existingReview : existingReviews) {
                        if(existingReview.getCommentId().equals(r.getCommentId())) {
                            if(existingReview.equals(r)) {
                                return false;
                            }
                            r.setId(existingReview.getId());
                            return true;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

        Iterable<Review> newReviews = repository.save(singleReviews);

        if (newReviews == null) {
            throw new ReviewsConflictException("Save reviews error");
        }

        List<Review> historyData = StreamSupport.stream(reviews.spliterator(), false)
                .filter((r) -> r.getTimestamp() == null).collect(Collectors.toList());

        if(historyData.size() > 0) {
            List<Review> dbHistoricalReviews = repository.findAllHistorical();

            if(dbHistoricalReviews.size() > 0) {
                dbHistoricalReviews = dbHistoricalReviews.stream().filter((review) -> {
                    Optional<Review> newDataOpt = historyData.stream()
                            .filter((h) -> review.getAppname().equals(h.getAppname()))
                            .findFirst();
                    if(newDataOpt.isPresent()) {
                        Review newData = newDataOpt.get();
                        review.setAmount(newData.getAmount());
                        review.setStarrating(newData.getStarrating());
                        historyData.remove(newData);
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
                repository.save(dbHistoricalReviews);
            }

            if(historyData.size() > 0) {
                repository.save(historyData);
            }
        }

        return getReviewIds(reviews);
    }

    @Override
    public ReviewDTO saveMirrorGateReview(ReviewDTO review) {
        Review toSave = new Review();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long id = System.currentTimeMillis();

        if(auth != null) {
            review.setAuthor((String) auth.getPrincipal());
            toSave.setAuthorName((String) auth.getPrincipal());
        }

        toSave.setAppname(MIRRORGATE);
        toSave.setStarrating(review.getRate());
        toSave.setComment(review.getComment());
        toSave.setTimestamp(id);
        toSave.setCommentId(Long.toString(id));
        toSave.setPlatform(Platform.Unknown);

        repository.save(toSave);

        updateHistoryForMirrorGateReview(toSave);

        return review;
    }

    private synchronized void updateHistoryForMirrorGateReview(Review toSave) {
        List<Review> historyList = repository.findAllByCommentIdIn(Arrays.asList(MIRRORGATE_COMMENT_ID));

        Review history;

        if(historyList.size() > 0) {
            history = historyList.get(0);
        } else {
            history = new Review();
            history.setPlatform(Platform.Unknown);
            history.setAppname(MIRRORGATE);
            history.setCommentId(MIRRORGATE_COMMENT_ID);
            history.setAmount(0);
        }

        double rating = history.getStarrating() *  history.getAmount();
        history.setAmount(history.getAmount() + 1);
        history.setStarrating((toSave.getStarrating() + rating)/history.getAmount());

        repository.save(history);
    }
}