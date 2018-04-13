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
    private String access_token;
    private String text;
    private String url;
    private String error;

    public boolean isOk() {
        return ok;
    }

    public SlackDTO setOk(boolean ok) {
        this.ok = ok;
        return this;
    }

    public String getAccess_token() {
        return access_token;
    }

    public SlackDTO setAccess_token(String access_token) {
        this.access_token = access_token;
        return this;
    }

    public String getText() {
        return text;
    }

    public SlackDTO setText(String text) {
        this.text = text;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public SlackDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getError() {
        return error;
    }

    public SlackDTO setError(String error) {
        this.error = error;
        return this;
    }

}
