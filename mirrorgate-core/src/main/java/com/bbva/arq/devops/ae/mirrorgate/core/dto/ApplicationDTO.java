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

package com.bbva.arq.devops.ae.mirrorgate.core.dto;

import com.bbva.arq.devops.ae.mirrorgate.core.utils.Platform;

import java.util.List;

public class ApplicationDTO {

    private String appname;
    private long votesTotal;
    private long ratingTotal;
    private long votes7Days;
    private long rating7Days;

    private Platform platform;
    private List<ReviewDTO> reviews;

    public ApplicationDTO() {

    }

    public String getAppname() {
        return appname;
    }

    public ApplicationDTO setAppname(String appname) {
        this.appname = appname;
        return this;
    }

    public Platform getPlatform() {
        return platform;
    }

    public ApplicationDTO setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public ApplicationDTO setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
        return this;
    }

    public long getVotesTotal() {
        return votesTotal;
    }

    public ApplicationDTO setVotesTotal(long votesTotal) {
        this.votesTotal = votesTotal;
        return this;
    }

    public long getRatingTotal() {
        return ratingTotal;
    }

    public ApplicationDTO setRatingTotal(long ratingTotal) {
        this.ratingTotal = ratingTotal;
        return this;
    }

    public long getVotes7Days() {
        return votes7Days;
    }

    public ApplicationDTO setVotes7Days(long votes7Days) {
        this.votes7Days = votes7Days;
        return this;
    }

    public long getRating7Days() {
        return rating7Days;
    }

    public ApplicationDTO setRating7Days(long rating7Days) {
        this.rating7Days = rating7Days;
        return this;
    }
}
