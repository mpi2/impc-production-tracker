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

import org.gentar.security.abac.subject.SystemSubject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

@Configuration
public class JPAAuditConfiguration
{
    @Bean
    public AuditorAware<String> auditorProvider()
    {
        return () -> {
            String userName = null;
            if (SecurityContextHolder.getContext().getAuthentication() != null)
            {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Object principal = auth.getPrincipal();
                SystemSubject userDetails = (SystemSubject) principal;
                userName = userDetails.getLogin();
            }
            else
            {
                userName = "Unknown";
            }
            return Optional.ofNullable(userName);
        };
    }
}
