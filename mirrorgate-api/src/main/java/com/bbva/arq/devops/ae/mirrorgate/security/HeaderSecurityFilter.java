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

package com.bbva.arq.devops.ae.mirrorgate.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class HeaderSecurityFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(HeaderSecurityFilter.class.getName());
    private static final String USER_HEADER = "X-Forwarded-User";

    @Override
    protected void doFilterInternal(
        final @Nullable HttpServletRequest request,
        final @Nullable HttpServletResponse response,
        final @Nullable FilterChain filterChain
    ) throws ServletException, IOException {

        final List<String> headerNames = new ArrayList<>();
        final Enumeration<?> headers = Objects.requireNonNull(request).getHeaderNames();

        while (headers.hasMoreElements()) {
            headerNames.add((String) headers.nextElement());
        }

        LOG.info("Method: {}, URI: {}", request.getMethod(), request.getRequestURI());
        LOG.info("Request headers {}", headerNames);

        final String xForwardedUser = request.getHeader(USER_HEADER);

        LOG.info("X-Forwarded-User header value {}", xForwardedUser);

        final Authentication auth = TokenCreator.createHeaderBasedToken(xForwardedUser);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Objects.requireNonNull(filterChain).doFilter(request, response);
    }
}
