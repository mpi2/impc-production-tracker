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
package org.gentar.security.abac.spring;

import org.gentar.security.abac.subject.SubjectRetriever;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.gentar.security.abac.policy.PolicyEnforcement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class allows to access from any part of the code to the ABAC logic.
 */

@Component
public class ContextAwarePolicyEnforcement
{
    private final PolicyEnforcement policy;
    private final SubjectRetriever subjectRetriever;

    public ContextAwarePolicyEnforcement(PolicyEnforcement policy, SubjectRetriever subjectRetriever)
    {
        this.policy = policy;
        this.subjectRetriever = subjectRetriever;
    }

    public void checkPermission(Object resource, String permission)
    {
        Map<String, Object> environment = new HashMap<>();

        environment.put("time", new Date());

        if(!policy.check(subjectRetriever.getSubject(), resource, permission, environment))
            throw new AccessDeniedException("Access is denied");
    }

    /**
     * Checks if the subject has a specific permission on a resource.
     * @param subject Object that represents the user for whom we want to check the permission.
     * @param resource The resource for which we evaluate the permission.
     * @param permission The name of the action we are evaluating.
     * @return True if the subject has permission. False otherwise.
     */
    public boolean hasPermission(Object subject, Object resource, String permission)
    {
        Map<String, Object> environment = new HashMap<>();
        environment.put("time", new Date());
        return policy.check(subject, resource, permission, environment);
    }

    /**
     * Checks if the user that is currently logged in the system has a specific permission on a resource.
     * @param resource The resource for which we evaluate the permission.
     * @param permission The name of the action we are evaluating.
     * @return True if the logged user has permission. False otherwise.
     */
    public boolean hasPermission(Object resource, String permission)
    {
        return hasPermission(subjectRetriever.getSubject(), resource, permission);
    }

    public boolean isUserAnonymous()
    {
        return subjectRetriever.isUserAnonymous();
    }
}
