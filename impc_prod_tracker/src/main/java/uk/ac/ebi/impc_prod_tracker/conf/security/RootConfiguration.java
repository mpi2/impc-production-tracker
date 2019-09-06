/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package uk.ac.ebi.impc_prod_tracker.conf.security;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uk.ac.ebi.impc_prod_tracker.conf.error_management.ExceptionHandlerFilter;
import uk.ac.ebi.impc_prod_tracker.conf.security.jwt.JwtTokenFilter;

/**
 * Sets the authentication mechanism for the end points.
 *
 * @author Mauricio Martinez
 */
@Configuration
@EnableWebSecurity
@EnableJpaAuditing
public class RootConfiguration extends WebSecurityConfigurerAdapter
{
    private JwtTokenFilter jwtTokenFilter;
    private ExceptionHandlerFilter exceptionHandlerFilter;


    public RootConfiguration(
        JwtTokenFilter jwtTokenFilter, ExceptionHandlerFilter exceptionHandlerFilter)
    {
        this.jwtTokenFilter = jwtTokenFilter;
        this.exceptionHandlerFilter = exceptionHandlerFilter;

    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
            .httpBasic().disable()
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/auth/signin").permitAll()
            .antMatchers("/api/hello").permitAll()
            .antMatchers("/api/plans").permitAll()
            .antMatchers("/api/projectSummaries").permitAll()
            .antMatchers("/api/projectSummaries/**").permitAll()
            .antMatchers("/api/projects/**").permitAll()
                .antMatchers("/download/projectSummaries/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlerFilter, JwtTokenFilter.class)
            .addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class)
        ;
    }
}
