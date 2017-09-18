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

import com.bbva.arq.devops.ae.mirrorgate.service.DashboardServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alfonso on 18/09/17.
 */
@Component
public class OneTimeETagGenerationFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(OneTimeETagGenerationFilter.class);


    private Filter filter = new ShallowEtagHeaderFilter();
    private Map<String,String> cache = new HashMap();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain
            chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("Just supports HTTP requests");
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String key = httpRequest.getPathTranslated();

        if(LOG.isDebugEnabled()) {
            LOG.debug("Fetching etag in cache for path: " + key);
        }

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
