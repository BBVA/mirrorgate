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

import static com.bbva.arq.devops.ae.mirrorgate.mapper.ReviewMapper.map;

import com.bbva.arq.devops.ae.mirrorgate.dto.ApplicationDTO;
import com.bbva.arq.devops.ae.mirrorgate.dto.ReviewDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.EventType;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;
import com.bbva.arq.devops.ae.mirrorgate.repository.ReviewRepository;
import com.bbva.arq.devops.ae.mirrorgate.support.Platform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final int LONG_TERM_MULTIPLIER = 3;
    private static final long DAY_IN_MS = (long) 1000 * 60 * 60 * 24;
    private static final String FB_NAMESPACE = "Mirrorgate/";
    private static final String FB_HISTORY_SUFFIX = "_history";

    private final ReviewRepository repository;
    private final EventService eventService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository repository, EventService eventService) {

        this.repository = repository;
        this.eventService = eventService;
    }

    @Override
    public List<ApplicationDTO> getAverageRateByAppNames(List<String> names, int daysShortTerm) {
        final List<ApplicationDTO> result = repository.getAppInfoByAppNames(names);

        final List<Review> history = repository.findHistoricalForApps(names);

        //Update merge the history review inside the result
        result.forEach((app) -> {
            final Optional<Review> historyReviewOpt = history.stream()
                .filter((r) -> app.getAppname().equals(r.getAppname()) && app.getPlatform() == r.getPlatform())
                .findFirst();
            if (historyReviewOpt.isPresent()) {
                final Review historyReview = historyReviewOpt.get();
                app.setVotesTotal(historyReview.getAmount())
                    .setUrl(historyReview.getUrl())
                    .setRatingTotal((long) (historyReview.getStarrating() * historyReview.getAmount()));
            }
        });

        final int daysLongTerm = daysShortTerm * LONG_TERM_MULTIPLIER;

        final Date dateShortTerm = new Date(System.currentTimeMillis() - (daysShortTerm * DAY_IN_MS));
        final Date dateLongTerm = new Date(System.currentTimeMillis() - (daysLongTerm * DAY_IN_MS));

        final List<ApplicationDTO> statsShortTerm = repository
            .getAverageRateByAppNamesAfterTimestamp(names, dateShortTerm.getTime());
        final List<ApplicationDTO> statsLongTerm = repository
            .getAverageRateByAppNamesAfterTimestamp(names, dateLongTerm.getTime());

        result.forEach((app) -> {
            final Optional<ApplicationDTO> appStatsShortTerm = statsShortTerm.stream()
                .filter((stat) ->
                    stat.getAppname().equals(app.getAppname()) && stat.getPlatform().equals(app.getPlatform())
                )
                .findFirst();
            final Optional<ApplicationDTO> appStatsLongTerm = statsLongTerm.stream()
                .filter((stat) ->
                    stat.getAppname().equals(app.getAppname()) && stat.getPlatform().equals(app.getPlatform())
                )
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

    private List<String> getReviewIds(final Iterable<Review> reviews) {
        final List<String> savedIDs = new ArrayList<>();

        reviews.forEach(request -> savedIDs.add(request.getCommentId()));

        return savedIDs;
    }

    @Override
    public List<String> saveAll(final Iterable<Review> reviews) {

        List<Review> singleReviews = StreamSupport.stream(reviews.spliterator(), false)
            .filter((r) -> r.getTimestamp() != null).collect(Collectors.toList());

        final List<Review> existingReviews = repository.findAllByCommentIdIn(
            singleReviews.stream().map(Review::getCommentId).collect(Collectors.toList())
        );

        singleReviews = singleReviews.stream()
            .filter((r) -> {
                //We exclude reviews that equal existing one and update the objectId for those
                // different and already in the DB while keeping the new ones
                for (final Review existingReview : existingReviews) {
                    if (existingReview.getCommentId().equals(r.getCommentId())) {
                        if (existingReview.equals(r)) {
                            return false;
                        }
                        r.setId(existingReview.getId());
                        return true;
                    }
                }
                return true;
            })
            .collect(Collectors.toList());

        final Iterable<Review> newReviews = repository.saveAll(singleReviews);
        eventService.saveEvents(newReviews, EventType.REVIEW);

        final List<Review> historyData = StreamSupport.stream(reviews.spliterator(), false)
            .filter((r) -> r.getTimestamp() == null).collect(Collectors.toList());

        if (! historyData.isEmpty()) {
            List<Review> dbHistoricalReviews = repository.findAllHistorical();

            if (! dbHistoricalReviews.isEmpty()) {
                dbHistoricalReviews = dbHistoricalReviews.stream().filter((review) -> {
                    final Optional<Review> newDataOpt = historyData.stream()
                        .filter((h) -> review.getAppname().equals(h.getAppname()))
                        .findFirst();
                    if (newDataOpt.isPresent()) {
                        final Review newData = newDataOpt.get();
                        review
                            .setAmount(newData.getAmount())
                            .setStarrating(newData.getStarrating());
                        historyData.remove(newData);
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
                repository.saveAll(dbHistoricalReviews);
            }

            if (! historyData.isEmpty()) {
                repository.saveAll(historyData);
            }
        }

        return getReviewIds(reviews);
    }

    @Override
    public ReviewDTO saveApplicationReview(final String appId, final ReviewDTO review) {
        final Review toSave = map(review);

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final long id = System.currentTimeMillis();

        if (auth != null) {
            toSave.setAuthorName((String) auth.getPrincipal());
        }

        toSave.setAppname(FB_NAMESPACE + appId)
            .setTimestamp(id)
            .setCommentId(Long.toString(id))
            .setPlatform(Platform.Unknown);

        final Review savedReview = repository.save(toSave);
        eventService.saveEvent(savedReview, EventType.REVIEW);

        updateHistoryForApplicationReview(toSave);

        return map(savedReview);
    }

    @Override
    public Iterable<Review> getReviewsByObjectId(final List<ObjectId> objectIds) {
        return repository.findAllById(objectIds);
    }

    private synchronized void updateHistoryForApplicationReview(final Review toSave) {
        final List<Review> historyList = repository.findAllByCommentIdIn(
            Collections.singletonList(toSave.getAppname() + FB_HISTORY_SUFFIX)
        );

        Review history;

        if (! historyList.isEmpty()) {
            history = historyList.get(0);
        } else {
            history = new Review()
                .setPlatform(Platform.Unknown)
                .setAppname(toSave.getAppname())
                .setCommentId(toSave.getAppname() + FB_HISTORY_SUFFIX)
                .setAmount(0);
        }

        final double rating = history.getStarrating() * history.getAmount();
        history.setAmount(history.getAmount() + 1)
            .setStarrating((toSave.getStarrating() + rating) / history.getAmount());

        repository.save(history);
    }
}