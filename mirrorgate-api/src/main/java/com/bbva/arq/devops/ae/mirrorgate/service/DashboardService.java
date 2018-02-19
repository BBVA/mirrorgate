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

import com.bbva.arq.devops.ae.mirrorgate.core.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.model.Build;
import com.bbva.arq.devops.ae.mirrorgate.model.Dashboard;
import com.bbva.arq.devops.ae.mirrorgate.model.Feature;
import com.bbva.arq.devops.ae.mirrorgate.model.ImageStream;
import java.io.InputStream;
import java.util.List;

/**
 * Dashboards service
 */
public interface DashboardService {

    /**
     * Get a Dashboard with a specific name
     *
     * @param name Name of the Dashboard.
     * @return Dashboard
     */
    DashboardDTO getDashboard(String name);

    /**
     * Get the repositories of a specific Dashboard
     *
     * @param name Name of the Dashboard
     * @return List of repositories
     */
    List<String> getReposByDashboardName(String name);

    /**
     * Get the admin users of a specific Dashboard
     *
     * @param name Name of the Dashboard
     * @return List of admin users
     */
    List<String> getAdminUsersByDashboardName(String name);

    /**
     * Get all names of applications related to a Dashboard.
     *
     * @param name Name of the Dashboard
     * @return A list with the name of applications of the Dashboard
     */
    List<String> getApplicationsByDashboardName(String name);

    /**
     * Get a list with all Dashboards with status Active
     *
     * @return A list of DashboardDTO
     */
    List<DashboardDTO> getActiveDashboards();

    /**
     * Mark a Dashboard as a Delete
     *
     * @param name
     */
    void deleteDashboard(String name);

    /**
     * Create a new Dashboard
     *
     * @param dashboard
     * @return Dashboard new persisted Dashboard
     */
    DashboardDTO newDashboard(DashboardDTO dashboard);

    /**
     * Updates a Dashboard
     *
     * @param dashboard
     * @param name
     * @return Dashboard persisted Dashboard or null if no existent
     */
    DashboardDTO updateDashboard(String name, DashboardDTO dashboard);

    /**
     * Saves an image related to the dashboard with the corresponding name
     *
     * @param name
     * @param uploadfile
     */
    void saveDashboardImage(String name, InputStream uploadfile);

    /**
     * Gets the image associated with a dashboard
     *
     * @param name
     * @return
     */
    ImageStream getDashboardImage(String name);

    List<Dashboard> getDashboardWithNames(List<String> dashboardNames);

    void createDashboardForBuildProject(Build build);
    void createDashboardForJiraTeam(String teamName);
}
