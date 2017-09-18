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

package com.bbva.arq.devops.ae.mirrorgate.model;

import java.io.InputStream;

/**
 * Created by alfonso on 18/09/17.
 */
public class ImageStream {

    private InputStream imageStream;

    private String etag;

    public InputStream getImageStream() {
        return imageStream;
    }

    public ImageStream setImageStream(InputStream imageStream) {
        this.imageStream = imageStream;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public ImageStream setEtag(String etag) {
        this.etag = etag;
        return this;
    }
}
