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

package com.bbva.arq.devops.ae.mirrorgate.dto;

import com.bbva.arq.devops.ae.mirrorgate.support.Platform;
import java.util.List;

public class ApplicationDTO {

    private String appname;
    private long votesTotal;
    private long ratingTotal;
    private long votesShortTerm;
    private long ratingShortTerm;
    private long votesLongTerm;
    private long ratingLongTerm;
    private int shortTermLength;
    private int longTermLength;


    private Platform platform;
    private List<ReviewDTO> reviews;
    private String url;

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

    public long getVotesShortTerm() {
        return votesShortTerm;
    }

    public ApplicationDTO setVotesShortTerm(long votesShortTerm) {
        this.votesShortTerm = votesShortTerm;
        return this;
    }

    public long getRatingShortTerm() {
        return ratingShortTerm;
    }

    public ApplicationDTO setRatingShortTerm(long ratingShortTerm) {
        this.ratingShortTerm = ratingShortTerm;
        return this;
    }

    public long getRatingLongTerm() {
        return ratingLongTerm;
    }

    public ApplicationDTO setRatingLongTerm(long ratingLongTerm) {
        this.ratingLongTerm = ratingLongTerm;
        return this;
    }

    public long getVotesLongTerm() {
        return votesLongTerm;
    }

    public ApplicationDTO setVotesLongTerm(long votesLongTerm) {
        this.votesLongTerm = votesLongTerm;
        return this;
    }

    public int getShortTermLength() {
        return shortTermLength;
    }

    public ApplicationDTO setShortTermLength(int shortTermLength) {
        this.shortTermLength = shortTermLength;
        return this;
    }

    public int getLongTermLength() {
        return longTermLength;
    }

    public ApplicationDTO setLongTermLength(int longTermLength) {
        this.longTermLength = longTermLength;
        return this;
    }

    public ApplicationDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUrl() {
        return url;
    }
}
