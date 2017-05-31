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

import com.bbva.arq.devops.ae.mirrorgate.security.HeaderSecurityFilter;
import com.bbva.arq.devops.ae.mirrorgate.security.MirrorGateAuthenticationProvider;
import com.bbva.arq.devops.ae.mirrorgate.security.SecurityAuthoritiesEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

/**
 * Application Configuration with basic http security
 */
@Configuration
@Profile("default")
@ComponentScan(basePackages = "com.bbva.arq.devops.ae.mirrorgate")
public class RestConfig {

    @Bean
    public WebSecurityConfigurerAdapter secureConfigurer() {
        return new WebSecurityConfigurerAdapterImpl();
    }

    private static class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .addFilterBefore(new HeaderSecurityFilter(), SecurityContextHolderAwareRequestFilter.class)
                    .cors()
                        .and()
                    .csrf()
                        .disable()
                    .authorizeRequests()
                        .antMatchers("/health").permitAll()
                        .antMatchers(HttpMethod.OPTIONS,"**").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/**").hasAuthority(SecurityAuthoritiesEnum.COLLECTOR.toString()).anyRequest().authenticated()
                        .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority(SecurityAuthoritiesEnum.COLLECTOR.toString()).anyRequest().authenticated()
                        .antMatchers(HttpMethod.GET, "/dashboards/**").hasAnyAuthority(SecurityAuthoritiesEnum.REGULAR.toString(), SecurityAuthoritiesEnum.SCREEN.toString())
                        .antMatchers(HttpMethod.POST, "/dashboards/**").hasAuthority(SecurityAuthoritiesEnum.REGULAR.toString()).anyRequest().authenticated()
                        .antMatchers(HttpMethod.DELETE, "/dashboards/**").hasAuthority(SecurityAuthoritiesEnum.REGULAR.toString()).anyRequest().authenticated()
                        .antMatchers(HttpMethod.PUT, "/dashboards/**").hasAuthority(SecurityAuthoritiesEnum.REGULAR.toString()).anyRequest().authenticated();
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(new MirrorGateAuthenticationProvider());
        }
    }
}
