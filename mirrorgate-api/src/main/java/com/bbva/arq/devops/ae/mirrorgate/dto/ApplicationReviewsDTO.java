/*
 * Copyright 2017 Banco Bilbao Vizcaya Argentaria, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbva.arq.devops.ae.mirrorgate.dto;

import com.bbva.arq.devops.ae.mirrorgate.support.Platform;

public class ApplicationReviewsDTO {

    private Platform platform;
    private String appId;
    private String appName;
    private String commentId;

    public Platform getPlatform() {
        return platform;
    }

    public ApplicationReviewsDTO setPlatform(final Platform platform) {
        this.platform = platform;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public ApplicationReviewsDTO setAppId(final String appId) {
        this.appId = appId;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public ApplicationReviewsDTO setAppName(final String appName) {
        this.appName = appName;
        return this;
    }

    public String getCommentId() {
        return commentId;
    }

    public ApplicationReviewsDTO setCommentId(final String commentId) {
        this.commentId = commentId;
        return this;
    }
}
