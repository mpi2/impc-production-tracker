package org.gentar.security.abac;

import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.abac.subject.AapSystemSubject;
import org.springframework.stereotype.Component;

@Component
public class ResourceAccessCheckerImpl<T> implements ResourceAccessChecker<T> {
    private final ContextAwarePolicyEnforcement policyEnforcement;
    private final AapSystemSubject aapSystemSubject;
    public ResourceAccessCheckerImpl(ContextAwarePolicyEnforcement policyEnforcement, AapSystemSubject aapSystemSubject) {
        this.policyEnforcement = policyEnforcement;
        this.aapSystemSubject = aapSystemSubject;
    }

    @Override
    public Resource<T> checkAccess(Resource<T> resource, String action)
    {
        if (resource == null)
        {
            return null;
        }

        Resource<T> resourceResult = resource;

        if (policyEnforcement.isUserAnonymous())
        {
            if (!resource.getResourcePrivacy().equals(ResourcePrivacy.PUBLIC))
            {
                resourceResult = null;
            }
        }
        else
        {
            if (policyEnforcement.hasPermission(resource, action))
            {
                resourceResult = resource;
            }
            else
            {
                resourceResult = resource.getRestrictedObject();
            }
        }

        return resourceResult;
    }



}
