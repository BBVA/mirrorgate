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

package com.bbva.arq.devops.ae.mirrorgate.mapper;

import com.bbva.arq.devops.ae.mirrorgate.dto.ReviewDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Review;

public class ReviewMapper {

    public static ReviewDTO map(Review source) {
        return map(source, new ReviewDTO());
    }

    private static ReviewDTO map(Review source, ReviewDTO target) {
        return target
                .setAuthor(source.getAuthorName())
                .setRate(source.getStarrating())
                .setTimestamp(source.getTimestamp() == null ? 0 : source.getTimestamp())
                .setUrl(source.getUrl())
                .setComment(source.getComment());
    }

    public static Review map(ReviewDTO source) {
        return map(source, new Review());
    }

    private static Review map(ReviewDTO source, Review target) {
        target.setTimestamp(source.getTimestamp());
        target.setAuthorName(source.getAuthor());
        target.setStarrating(source.getRate());
        target.setComment(source.getComment());
        target.setUrl(source.getUrl());

        return target;
    }
}
