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
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.repository.ReviewRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.Platform;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.bbva.arq.devops.ae.mirrorgate.mapper.ReviewMapper.map;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final int LONG_TERM_MULTIPLIER = 3;
    private static final long DAY_IN_MS = (long) 1000 * 60 * 60 * 24;
    private static final String FB_NAMESPACE = "Mirrorgate/";
    private static final String FB_HISTORY_SUFFIX = "_history";

    private final ReviewRepository repository;
    private final EventService eventService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository repository, EventService eventService){

        this.repository = repository;
        this.eventService = eventService;
    }

    @Override
    public List<ApplicationDTO> getAverageRateByAppNames(List<String> names, int daysShortTerm) {
        List<ApplicationDTO> result = repository.getAppInfoByAppNames(names);

        List<Review> history = repository.findHistoricalForApps(names);

        //Update merge the history review inside the result
        result.forEach((app) -> {
            Optional<Review> historyReviewOpt = history.stream()
                    .filter((r) -> app.getAppname().equals(r.getAppname()) && app.getPlatform() == r.getPlatform())
                    .findFirst();
            if(historyReviewOpt.isPresent()) {
                Review historyReview = historyReviewOpt.get();
                app.setVotesTotal(historyReview.getAmount())
                    .setUrl(historyReview.getUrl())
                    .setRatingTotal((long) (historyReview.getStarrating() * historyReview.getAmount()));
            }
        });

        int daysLongTerm = daysShortTerm * LONG_TERM_MULTIPLIER;

        Date dateShortTerm = new Date(System.currentTimeMillis() - (daysShortTerm * DAY_IN_MS));
        Date dateLongTerm = new Date(System.currentTimeMillis() - (daysLongTerm * DAY_IN_MS));

        List<ApplicationDTO> statsShortTerm = repository.getAverageRateByAppNamesAfterTimestamp(names, dateShortTerm.getTime());
        List<ApplicationDTO> statsLongTerm = repository.getAverageRateByAppNamesAfterTimestamp(names, dateLongTerm.getTime());

        result.forEach((app) -> {
            Optional<ApplicationDTO> appStatsShortTerm = statsShortTerm.stream()
                    .filter((stat) -> stat.getAppname().equals(app.getAppname()) && stat.getPlatform().equals(app.getPlatform()))
                    .findFirst();
            Optional<ApplicationDTO> appStatsLongTerm = statsLongTerm.stream()
                    .filter((stat) -> stat.getAppname().equals(app.getAppname()) && stat.getPlatform().equals(app.getPlatform()))
                    .findFirst();

            appStatsShortTerm.ifPresent(applicationDTO -> app.setRatingShortTerm(applicationDTO.getRatingShortTerm())
                .setVotesShortTerm(applicationDTO.getVotesShortTerm())
                .setShortTermLength(daysShortTerm));
            //Ugly hack... we use the shortTerm to return the data even if it's for longTerm :-(
            appStatsLongTerm.ifPresent(applicationDTO -> app.setRatingLongTerm(applicationDTO.getRatingShortTerm())
                .setVotesLongTerm(applicationDTO.getVotesShortTerm())
                .setLongTermLength(daysLongTerm));

        });

        return result;
    }

    private List<String> getReviewIds(Iterable<Review> reviews) {
        List<String> savedIDs = new ArrayList<>();

        reviews.forEach(request -> savedIDs.add(request.getCommentId()));

        return savedIDs;
    }

    @Override
    public List<String> saveAll(Iterable<Review> reviews) {

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

        Iterable<Review> newReviews = repository.saveAll(singleReviews);
        eventService.saveEvents(newReviews, EventType.REVIEW);

        List<Review> historyData = StreamSupport.stream(reviews.spliterator(), false)
                .filter((r) -> r.getTimestamp() == null).collect(Collectors.toList());

        if(!historyData.isEmpty()) {
            List<Review> dbHistoricalReviews = repository.findAllHistorical();

            if(!dbHistoricalReviews.isEmpty()) {
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
                repository.saveAll(dbHistoricalReviews);
            }

            if(!historyData.isEmpty()) {
                repository.saveAll(historyData);
            }
        }

        return getReviewIds(reviews);
    }

    @Override
    public ReviewDTO saveApplicationReview(String appId, ReviewDTO review) {
        Review toSave = map(review);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long id = System.currentTimeMillis();

        if(auth != null) {
            toSave.setAuthorName((String) auth.getPrincipal());
        }

        toSave.setAppname(FB_NAMESPACE + appId);
        toSave.setTimestamp(id);
        toSave.setCommentId(Long.toString(id));
        toSave.setPlatform(Platform.Unknown);

        Review savedReview = repository.save(toSave);
        eventService.saveEvent(savedReview, EventType.REVIEW);

        updateHistoryForApplicationReview(toSave);

        return map(savedReview);
    }

    @Override
    public Iterable<Review> getReviewsByObjectId(List<ObjectId> objectIds){

        return repository.findAllById(objectIds);
    }

    private synchronized void updateHistoryForApplicationReview(Review toSave) {
        List<Review> historyList = repository.findAllByCommentIdIn(Collections.singletonList(toSave.getAppname() + FB_HISTORY_SUFFIX));

        Review history;

        if(!historyList.isEmpty()) {
            history = historyList.get(0);
        } else {
            history = new Review();
            history.setPlatform(Platform.Unknown);
            history.setAppname(toSave.getAppname());
            history.setCommentId(toSave.getAppname() + FB_HISTORY_SUFFIX);
            history.setAmount(0);
        }

        double rating = history.getStarrating() *  history.getAmount();
        history.setAmount(history.getAmount() + 1);
        history.setStarrating((toSave.getStarrating() + rating)/history.getAmount());

        repository.save(history);
    }
}