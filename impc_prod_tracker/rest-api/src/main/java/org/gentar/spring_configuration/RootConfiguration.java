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

import org.gentar.BaseEntity;
import org.gentar.error_management.ExceptionHandlerFilter;
import org.gentar.security.jwt.JwtTokenFilter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

/**
 * Sets the authentication mechanism for the end points.
 *
 * @author Mauricio Martinez
 */
@Configuration
@EnableWebSecurity
@EnableJpaAuditing
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
        ModelMapper modelMapper = new ModelMapper();


        modelMapper.getConfiguration().setPropertyCondition(context -> {
            if (context.getParent() != null && 
                context.getParent().getDestination() != null &&
                context.getParent().getDestination() instanceof BaseEntity &&
                context.getMapping().getLastDestinationProperty() != null) {
                
                String propertyName = context.getMapping().getLastDestinationProperty().getName();

                return !propertyName.equals("createdAt") &&
                       !propertyName.equals("lastModified") &&
                       !propertyName.equals("createdBy") &&
                       !propertyName.equals("lastModifiedBy");
            }
            return true;
        });
        
        return modelMapper;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("OPTIONS");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedHeader("X-Requested-With");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Origin");
        configuration.addAllowedHeader("Accept");
        configuration.addAllowedHeader("Access-Control-Request-Method");
        configuration.addAllowedHeader("Access-Control-Request-Headers");
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/docs/**").permitAll()
                .requestMatchers("/auth/signin").permitAll()
                .requestMatchers("/api/conf").permitAll()
                .requestMatchers("/api/projects/**").permitAll()
                .requestMatchers("/api/plans/**").permitAll()
                .requestMatchers("/api/outcomes/**").permitAll()
                .requestMatchers("/api/mutations/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/people/requestPasswordReset").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/people/requestPasswordReset/").permitAll()
                .requestMatchers("/api/reports/**").permitAll()
                .requestMatchers("/api/distributionNetwork/**").permitAll()
                .requestMatchers("/api/glt_production_numbers/**").permitAll()
                .requestMatchers("/api/format/**").access(new WebExpressionAuthorizationManager("hasPermission(null, 'CDA_AND_ADMIN')"))
                .requestMatchers("/reports/**").permitAll()
                .requestMatchers("/tracking-api/targRepGenes/**").permitAll()
                .requestMatchers("/tracking-api/mutations/**").permitAll()
                .requestMatchers("/tracking-api/genes/**").permitAll()
                .requestMatchers("/tracking-api/plans/**").permitAll()
                .requestMatchers("/tracking-api/projects/**").permitAll()
                .requestMatchers("/tracking-api/**").access(new WebExpressionAuthorizationManager("hasPermission(null, 'CDA_AND_ADMIN')"))
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlerFilter, JwtTokenFilter.class)
        ;

        return http.build();
    }

}