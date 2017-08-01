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
package com.bbva.arq.devops.ae.mirrorgate.repository;

import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationDTO;
import com.bbva.arq.devops.ae.mirrorgate.core.dto.ApplicationReviewsDTO;
import java.util.List;

public interface ReviewRepositoryCustom {

    /**
     * Get the average of star rating of every application form a list of names.
     *
     * @param names A list with the name of the applications
     * @return Al List of Application objects with name and rate of every application
     */
    List<ApplicationDTO> getAverageRateByAppNames(List<String> names);

    /**
     * Get the last review for each application
     *
     * @param names A list with the name of the applications
     * @return Al List of ApplicationReviews objects with reviews data
     */
    List<ApplicationReviewsDTO> getLastReviewPerApplication(List<String> names);

    /**
     * Get the average of star rating after the specified time of every application form a list of names.
     *
     * @param names A list with the name of the applications
     * @return the rating average
     */
    List<ApplicationDTO> getAverageRateByAppNamesAfterTimestamp(List<String> names, Long timestamp);

}
