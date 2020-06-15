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

public class SlackDTO {

    private boolean ok;
    private String accessToken;
    private String text;
    private String url;
    private String error;

    public boolean isOk() {
        return ok;
    }

    public SlackDTO setOk(final boolean ok) {
        this.ok = ok;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public SlackDTO setAccess_token(final String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getText() {
        return text;
    }

    public SlackDTO setText(final String text) {
        this.text = text;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public SlackDTO setUrl(final String url) {
        this.url = url;
        return this;
    }

    public String getError() {
        return error;
    }

    public SlackDTO setError(final String error) {
        this.error = error;
        return this;
    }

}
