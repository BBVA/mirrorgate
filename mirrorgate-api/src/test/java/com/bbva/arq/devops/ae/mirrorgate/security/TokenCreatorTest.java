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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class TokenCreatorTest {

    @Test
    public void testScreenUser() {
        final MirrorgateAuthenticationToken token = TokenCreator.createHeaderBasedToken("testuser@bbva.com");
        final SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority(
            SecurityAuthoritiesEnum.REGULAR.toString()
        );

        assertTrue(token.getCredentials().contains(expectedAuthority));
    }

    @Test
    public void testEmptyHeader() {
        final MirrorgateAuthenticationToken token = TokenCreator.createHeaderBasedToken("");
        final SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority(
            SecurityAuthoritiesEnum.COLLECTOR.toString()
        );

        assertTrue(token.getCredentials().contains(expectedAuthority));
    }

    @Test
    public void testNullHeader() {
        final MirrorgateAuthenticationToken token = TokenCreator.createHeaderBasedToken(null);
        final SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority(
            SecurityAuthoritiesEnum.COLLECTOR.toString()
        );

        assertTrue(token.getCredentials().contains(expectedAuthority));
    }

    @Test
    public void testRegularUser() {
        final MirrorgateAuthenticationToken token = TokenCreator.createHeaderBasedToken("ANONYMOUS");
        final SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority(
            SecurityAuthoritiesEnum.SCREEN.toString()
        );

        assertTrue(token.getCredentials().contains(expectedAuthority));
    }

    @Test
    public void testCollectorUser() {
        final MirrorgateAuthenticationToken token = TokenCreator.createHeaderBasedToken("COLLECTOR");
        final SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority(
            SecurityAuthoritiesEnum.COLLECTOR.toString()
        );

        assertTrue(token.getCredentials().contains(expectedAuthority));
    }

}
