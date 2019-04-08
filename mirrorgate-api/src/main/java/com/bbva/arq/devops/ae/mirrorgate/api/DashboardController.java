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
package com.bbva.arq.devops.ae.mirrorgate.api;

import com.bbva.arq.devops.ae.mirrorgate.dto.DashboardDTO;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardService;
import com.bbva.arq.devops.ae.mirrorgate.service.DashboardServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Dashboards controller.
 */
@RestController
public class DashboardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @RequestMapping(
        value = {"/dashboards/{name}/details", "/api/dashboards/{name}/details"},
        method = GET,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DashboardDTO> getDashboard(@PathVariable("name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(dashboardService.getDashboard(name));
    }

    @RequestMapping(
        value = {"/dashboards/{name}", "/api/dashboards/{name}"},
        method = DELETE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> deleteDashboard(@PathVariable("name") String name) {
        dashboardService.deleteDashboard(name);
        return ResponseEntity.status(HttpStatus.OK).body("Dashboard was deleted successfully");
    }

    @RequestMapping(
        value = {"/dashboards", "/api/dashboards"},
        method = GET,
        produces = APPLICATION_JSON_VALUE
    )
    public List<DashboardDTO> getActiveDashboards(@RequestParam(name = "transient", required = false, defaultValue = "false") boolean alsoTransient) {
        return !alsoTransient ? dashboardService.getActiveDashboards() : dashboardService.getActiveAndTransientDashboards();
    }

    @RequestMapping(
        value = "/dashboards", method = POST,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DashboardDTO> newDashboard(@Valid @RequestBody DashboardDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dashboardService.newDashboard(request));
    }

    @RequestMapping(
        value = "/api/dashboards", method = POST,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DashboardDTO> newTransientDashboard(@Valid @RequestBody DashboardDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dashboardService.newTransientDashboard(request));
    }

    @RequestMapping(
        value = {"/dashboards/{name}", "/api/dashboards/{name}"}, method = PUT,
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<DashboardDTO> updateDashboard(
        @PathVariable("name") String name,
        @Valid @RequestBody DashboardDTO request) {
        DashboardDTO updatedDashboard = dashboardService.updateDashboard(name, request);

        return ResponseEntity.ok(updatedDashboard);

    }

    @RequestMapping(value = "/dashboards/{name}/image", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(
        @PathVariable("name") String name,
        @RequestParam("uploadfile") MultipartFile uploadfile) {
        try {
            dashboardService.saveDashboardImage(name, uploadfile.getInputStream());
            return ResponseEntity.ok("Saved successfully");
        } catch (IOException e) {
            LOGGER.error("Error getting input stream", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @RequestMapping(value = "/dashboards/{name}/image", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getFile(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("name") String name) {

        InputStreamResource resource = dashboardService.getDashboardImage(name);

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        try {

            byte[] content = StreamUtils.copyToByteArray(resource.getInputStream());

            String eTag = DigestUtils.md5Hex(content);
            String expectedETag = request.getHeader(HttpHeaders.IF_NONE_MATCH);

            if (eTag.equals(expectedETag)) {
                return ResponseEntity
                    .status(HttpServletResponse.SC_NOT_MODIFIED)
                    .build();
            }

            return ResponseEntity
                .ok()
                .contentLength(resource.contentLength())
                .lastModified(resource.lastModified())
                .cacheControl(CacheControl.maxAge(0, TimeUnit.MILLISECONDS).mustRevalidate())
                .eTag(eTag)
                .body(content);
        } catch (IOException e) {
            LOGGER.error("Error copying streams for dashboard " + name, e);
            return ResponseEntity
                .status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                .build();
        }
    }

}
