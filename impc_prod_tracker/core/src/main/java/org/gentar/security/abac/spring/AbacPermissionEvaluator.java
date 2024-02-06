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
package org.gentar.security.abac.spring;

import org.gentar.security.abac.subject.SubjectRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.gentar.security.abac.policy.PolicyEnforcement;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AbacPermissionEvaluator implements PermissionEvaluator
{
    private static final Logger logger = LoggerFactory.getLogger(AbacPermissionEvaluator.class);
    private final SubjectRetriever subjectRetriever;

    private final PolicyEnforcement policy;

    public AbacPermissionEvaluator(PolicyEnforcement policy, SubjectRetriever subjectRetriever)
    {
        this.policy = policy;
        this.subjectRetriever = subjectRetriever;
    }

    @Override
    public boolean hasPermission(Authentication authentication , Object targetDomainObject, Object permission)
    {
        Map<String, Object> environment = new HashMap<>();

        environment.put("time", new Date());

        logger.debug("hasPersmission({}, {}, {})", subjectRetriever.getSubject(), targetDomainObject, permission);
        return policy.check(subjectRetriever.getSubject(), targetDomainObject, permission, environment);
    }

    @Override
    public boolean hasPermission(
        Authentication authentication, Serializable targetId, String targetType, Object permission)
    {
        return false;
    }
}
