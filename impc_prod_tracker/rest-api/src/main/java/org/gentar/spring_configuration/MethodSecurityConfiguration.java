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

import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.gentar.security.abac.spring.AbacPermissionEvaluator;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration
{
    AbacPermissionEvaluator permissionEvaluator;
    public MethodSecurityConfiguration(AbacPermissionEvaluator permissionEvaluator)
    {
        this.permissionEvaluator = permissionEvaluator;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler()
    {
        DefaultMethodSecurityExpressionHandler result = new DefaultMethodSecurityExpressionHandler();
        result.setPermissionEvaluator(permissionEvaluator);
        return result;
    }
}
