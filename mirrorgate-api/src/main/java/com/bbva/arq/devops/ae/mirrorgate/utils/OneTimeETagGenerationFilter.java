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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import java.util.regex.Pattern;

/**
 * Created by alfonso on 18/09/17.
 */

public class OneTimeETagGenerationFilter extends GenericFilterBean {

    private final Filter filter = new ShallowEtagHeaderFilter();
    private final Map<String, String> cache = new HashMap();

    private static final Map<Pattern, Integer> TIME_FOR_URL = new HashMap(){{
        put(Pattern.compile(".*-reved-.*"), 31536000);
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

        for(Pattern pattern : TIME_FOR_URL.keySet()) {
            if(pattern.matcher(httpRequest.getRequestURI()).matches()) {
                cacheTime = TIME_FOR_URL.get(pattern);
            }
        }
        httpResponse.setHeader(HttpHeaders.CACHE_CONTROL, "max-age="+cacheTime+", must-revalidate");

        httpRequest.getRequestURI();

        String key = httpRequest.getPathTranslated();
        if(cache.containsKey(key)) {
            String expectedEtag = httpRequest.getHeader(HttpHeaders.IF_NONE_MATCH);
            String currentEtag = cache.get(key);
            if(expectedEtag != null && expectedEtag.equals(currentEtag)) {
                httpResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
        }

        filter.doFilter(request, response, chain);
        String etag = httpResponse.getHeader(HttpHeaders.ETAG);
        cache.put(key, etag);
    }

}
