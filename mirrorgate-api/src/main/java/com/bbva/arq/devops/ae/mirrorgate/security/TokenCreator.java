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

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

class TokenCreator {

    private static final Logger LOG = LoggerFactory.getLogger(TokenCreator.class.getName());

    private TokenCreator(){}

    public static MirrorgateAuthenticationToken createHeaderBasedToken(final String headerValue) {

        GrantedAuthority authority;

        // We keep empty header for back compatibility
        if (StringUtils.isEmpty(headerValue) || headerValue.contains("COLLECTOR")) {

            authority = new SimpleGrantedAuthority(SecurityAuthoritiesEnum.COLLECTOR.toString());

        } else {

            if (headerValue.contains("ANONYMOUS")) {
                authority = new SimpleGrantedAuthority(SecurityAuthoritiesEnum.SCREEN.toString());
            } else {
                authority = new SimpleGrantedAuthority(SecurityAuthoritiesEnum.REGULAR.toString());
            }

        }

        LOG.info("Role assigned: " + authority.getAuthority());

        return new MirrorgateAuthenticationToken(headerValue, Collections.singletonList(authority));
    }

}
