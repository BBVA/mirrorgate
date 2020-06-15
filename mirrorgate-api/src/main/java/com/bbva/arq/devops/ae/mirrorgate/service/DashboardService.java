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

package com.bbva.arq.devops.ae.mirrorgate.service;

import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import java.io.InputStream;
import java.util.List;
import org.springframework.core.io.InputStreamResource;

/**
 * Dashboards service.
 */
public interface DashboardService {

    /**
     * Get a Dashboard with a specific name.
     *
     * @param name Name of the Dashboard.
     * @return Dashboard
     */
    DashboardDTO getDashboard(final String name);

    /**
     * Get all names of applications related to a Dashboard.
     *
     * @param name Name of the Dashboard
     * @return A list with the name of applications of the Dashboard
     */
    List<String> getApplicationsByDashboardName(final String name);

    /**
     * Get a list with all active and transient dashboards.
     *
     * @return A list of DashboardDTO
     */
    List<DashboardDTO> getActiveAndTransientDashboards();

    /**
     * Get a list with all Dashboards with status Active.
     *
     * @return A list of DashboardDTO
     */
    List<DashboardDTO> getActiveDashboards();

    /**
     * Mark a Dashboard as a Delete.
     *
     * @param name Dashboard's name
     */
    void deleteDashboard(final String name);

    /**
     * Create a new Dashboard.
     *
     * @param dashboard Data Transfer Object of the Dashboard
     * @return Dashboard new persisted Dashboard
     */
    DashboardDTO newDashboard(final DashboardDTO dashboard);

    /**
     * Create a new Transient Dashboard.
     *
     * @param dashboard Data Transfer Object of the Dashboard
     * @return Dashboard new persisted Dashboard
     */
    DashboardDTO newTransientDashboard(final DashboardDTO dashboard);

    /**
     * Updates a Dashboard.
     *
     * @param dashboard Data Transfer Object of the Dashboard
     * @param name      Dashboard's name
     * @return Dashboard persisted Dashboard or null if no existent
     */
    DashboardDTO updateDashboard(final String name, final DashboardDTO dashboard);

    /**
     * Saves an image related to the dashboard with the corresponding name.
     *
     * @param name       Dashboard's name
     * @param uploadFile Dashboard's logo as an InputStream
     */
    void saveDashboardImage(final String name, final InputStream uploadFile);

    /**
     * Gets the image associated with a dashboard.
     *
     * @param name Dashboard's name
     * @return A stream of the image
     */
    InputStreamResource getDashboardImage(final String name);

    List<Dashboard> getDashboardWithNames(final List<String> dashboardNames);

    void createDashboardForBuildProject(final Build build);

    void createDashboardForJiraTeam(final String teamName);

}
