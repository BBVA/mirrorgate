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

package com.bbva.arq.devops.ae.mirrorgate.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by alfonso on 18/09/17.
 */

public class OneTimeETagGenerationFilter extends GenericFilterBean {

    private final Filter filter = new ShallowEtagHeaderFilter();
    private final Map<String, String> cache = new HashMap<>();

    private static final Map<Pattern, Integer> TIME_FOR_URL = new HashMap<>() {{
        put(Pattern.compile(".*-reved-.*"), 31536000);
        put(Pattern.compile(".*\\.css"), 60 * 60 * 24 * 7);
        put(Pattern.compile(".*\\.png"), 60 * 60 * 24 * 7);
        put(Pattern.compile(".*/bower_components/.*"), 60 * 60 * 24 * 7);
        put(Pattern.compile(".*/fonts/.*"), 60 * 60 * 24 * 7);
    }};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("Just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        int cacheTime = 0;

        for (Map.Entry<Pattern, Integer> entry : TIME_FOR_URL.entrySet()) {
            if (entry.getKey().matcher(httpRequest.getRequestURI()).matches()) {
                cacheTime = TIME_FOR_URL.get(entry.getKey());
            }
        }
        httpResponse.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=" + cacheTime + ", must-revalidate");

        httpRequest.getRequestURI();

        String key = httpRequest.getPathTranslated();
        if (cache.containsKey(key)) {
            String expectedETag = httpRequest.getHeader(HttpHeaders.IF_NONE_MATCH);
            String currentETag = cache.get(key);
            if (expectedETag != null && expectedETag.equals(currentETag)) {
                httpResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
        }

        filter.doFilter(request, response, chain);
        String eTag = httpResponse.getHeader(HttpHeaders.ETAG);
        cache.put(key, eTag);
    }

}
