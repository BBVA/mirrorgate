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
    private double rate;
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

    public double getRate() {
        return rate;
    }

    public ApplicationDTO setRate(double rate) {
        this.rate = rate;
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
}
