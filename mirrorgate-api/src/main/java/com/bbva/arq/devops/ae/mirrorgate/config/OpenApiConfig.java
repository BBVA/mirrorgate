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

package com.bbva.arq.devops.ae.mirrorgate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(
        @Value("${application.version}") String version,
        @Value("${application.description}") String description
    ) {
        return new OpenAPI()
            .info(new Info()
                .title("MirrorGate API")
                .version(version)
                .description(description)
            );
    }

    @Bean
    public GroupedOpenApi dashboardsAPI() {
        return GroupedOpenApi.builder()
            .group("dashboards-api")
            .pathsToMatch("/**")
            .pathsToExclude("/api/**")
            .build();
    }

    @Bean
    public GroupedOpenApi collectorsAPI() {
        return GroupedOpenApi.builder()
            .group("collectors-api")
            .pathsToMatch("/api/**")
            .build();
    }
}
