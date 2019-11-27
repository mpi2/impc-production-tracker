package org.gentar.security.abac;

import org.springframework.stereotype.Component;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;

@Component
public class ResourceAccessCheckerImpl<T> implements ResourceAccessChecker<T>
{
    private ContextAwarePolicyEnforcement policyEnforcement;

    public ResourceAccessCheckerImpl(ContextAwarePolicyEnforcement policyEnforcement)
    {
        this.policyEnforcement = policyEnforcement;
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
