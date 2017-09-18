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

package com.bbva.arq.devops.ae.mirrorgate.config;

import com.bbva.arq.devops.ae.mirrorgate.utils.OneTimeETagGenerationFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.catalina.servlet4preview.http.HttpFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by alfonso on 28/05/17.
 */

@Configuration
public class Config {

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        return builder;
    }

    @Bean
    public FilterRegistrationBean shallowEtagHeaderFilter() {
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(new OneTimeETagGenerationFilter());
        frb.addUrlPatterns(
                "/backoffice",
                "*.html",
                "*.js",
                "*.css",
                "/fonts",
                "/bower_components",
                "/components",
                "/img"
        );
        return frb;
    }
}
