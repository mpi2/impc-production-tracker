/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 * <p>
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.spring_configuration;

import org.gentar.error_management.ExceptionHandlerFilter;
import org.gentar.security.jwt.JwtTokenFilter;
import org.gentar.spring_configuration.filters.CORSFilter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Sets the authentication mechanism for the end points.
 *
 * @author Mauricio Martinez
 */
@Configuration
@EnableWebSecurity
@EnableJpaAuditing
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL })
public class RootConfiguration
{
    private final JwtTokenFilter jwtTokenFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    public RootConfiguration(
        JwtTokenFilter jwtTokenFilter, ExceptionHandlerFilter exceptionHandlerFilter)
    {
        this.jwtTokenFilter = jwtTokenFilter;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .requestMatchers("/docs/**").permitAll()
            .requestMatchers("/auth/signin").permitAll()
            .requestMatchers("/api/conf").permitAll()
            .requestMatchers("/api/projects/**").permitAll()
            .requestMatchers("/api/plans/**").permitAll()
            .requestMatchers("/api/outcomes/**").permitAll()
            .requestMatchers("/api/mutations/**").permitAll()
            .requestMatchers("/api/people/requestPasswordReset").permitAll()
            .requestMatchers("/api/reports/**").permitAll()
            .requestMatchers("/api/distributionNetwork/**").permitAll()
            .requestMatchers("/api/glt_production_numbers/**").permitAll()
            .requestMatchers("/api/format/**").access("hasPermission(null, 'CDA_AND_ADMIN')")
            .requestMatchers("/reports/**").permitAll()
            .requestMatchers("/tracking-api/**").access("hasPermission(null, 'CDA_AND_ADMIN')")
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlerFilter, JwtTokenFilter.class)
            .addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class)
        ;

        return http.build();
    }

}