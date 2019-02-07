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
package com.bbva.arq.devops.ae.mirrorgate.model;

import com.bbva.arq.devops.ae.mirrorgate.support.Platform;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * Market reviews model.
 * <p>
 * Collectors:
 * Google Play Store, Apple App Store
 */
@Document(collection = "reviews")
public class Review extends BaseModel {

    @Indexed
    private Long timestamp;

    @Indexed
    private Platform platform;

    @Indexed
    private String commentId;

    @Indexed
    private String appname;

    private String authorName;
    private double starrating;
    private String comment;

    private String url;

    private String commentTitle;
    private int amount = 1;

    public String getCommentTitle() {
        return commentTitle;
    }

    public Review setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
        return this;
    }

    public String getCommentId() {
        return commentId;
    }

    public Review setCommentId(String commentId) {
        this.commentId = commentId;
        return this;
    }

    public String getAppname() {
        return appname;
    }

    public Review setAppname(String appname) {
        this.appname = appname;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Review setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Review setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public double getStarrating() {
        return starrating;
    }

    public Review setStarrating(double starrating) {
        this.starrating = starrating;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Review setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Platform getPlatform() {
        return platform;
    }

    public Review setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public Review setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, platform, commentId, appname, authorName, starrating, comment, url, commentTitle, amount);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Review && equals((Review) o);
    }

    public boolean equals(Review review) {

        return this.getPlatform() == review.getPlatform() &&
            this.getStarrating() == review.getStarrating() &&
            stringEquals(this.getCommentId(), review.getCommentId()) &&
            stringEquals(this.getAppname(), review.getAppname()) &&
            stringEquals(this.getAuthorName(), review.getAuthorName()) &&
            stringEquals(this.getCommentTitle(), review.getCommentTitle()) &&
            stringEquals(this.getComment(), review.getComment());

    }

    private static boolean stringEquals(String s1, String s2) {
        return (s1 == null && s2 == null) || (s1 != null && s1.equals(s2));
    }

    public String getUrl() {
        return url;
    }

    public Review setUrl(String url) {
        this.url = url;
        return this;
    }
}
