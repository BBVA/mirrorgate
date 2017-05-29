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

public class ApplicationDTO {

    private String appname;
    private double rate;
    private Platform platform;
    private String last_review_author;
    private double last_review_rate;
    private long last_review_timestamp;
    private String last_review_comment;

    public ApplicationDTO(String appname, double rate, Platform platform, String last_review_author, double last_review_rate, long last_review_timestamp, String last_review_comment) {
        this.appname = appname;
        this.rate = rate;
        this.platform = platform;
        this.last_review_author = last_review_author;
        this.last_review_rate = last_review_rate;
        this.last_review_timestamp = last_review_timestamp;
        this.last_review_comment = last_review_comment;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getLast_review_author() {
        return last_review_author;
    }

    public void setLast_review_author(String last_review_author) {
        this.last_review_author = last_review_author;
    }

    public double getLast_review_rate() {
        return last_review_rate;
    }

    public void setLast_review_rate(double last_review_rate) {
        this.last_review_rate = last_review_rate;
    }

    public long getLast_review_timestamp() {
        return last_review_timestamp;
    }

    public void setLast_review_timestamp(long last_review_timestamp) {
        this.last_review_timestamp = last_review_timestamp;
    }

    public String getLast_review_comment() {
        return last_review_comment;
    }

    public void setLast_review_comment(String last_review_comment) {
        this.last_review_comment = last_review_comment;
    }

}
