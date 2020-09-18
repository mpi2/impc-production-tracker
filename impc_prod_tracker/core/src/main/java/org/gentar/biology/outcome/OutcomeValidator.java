package org.gentar.biology.outcome;

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.PermissionService;
import org.springframework.stereotype.Component;

@Component
public class OutcomeValidator
{
    private final ContextAwarePolicyEnforcement policyEnforcement;
    private static final String CREATION_ERROR_MESSAGE =
        "You cannot create an outcome associated to the plan %s because you do not have permissions " +
            "to edit that plan.";
    private static final String UPDATE_ERROR_MESSAGE =
        "You cannot edit the outcome %s because it is associated to the plan %s and you do not have" +
            " permissions to edit that plan";

    public OutcomeValidator(ContextAwarePolicyEnforcement policyEnforcement)
    {
        this.policyEnforcement = policyEnforcement;
    }

    // To create/edit an outcome the user needs to have permission to edit a plan
    public void validateCreationPermission(Outcome outcome)
    {
        if (!canEditPlan(outcome))
        {
            throw new UserOperationFailedException(
                String.format(CREATION_ERROR_MESSAGE, outcome.getPlan().getPin()));
        }
    }

    public void validateUpdatePermission(Outcome outcome)
    {
        if (!policyEnforcement.hasPermission(outcome.getPlan(), PermissionService.UPDATE_PLAN_ACTION))
        {
            throw new UserOperationFailedException(
                String.format(UPDATE_ERROR_MESSAGE, outcome.getTpo(), outcome.getPlan().getPin()));
        }
    }

    private boolean canEditPlan(Outcome outcome)
    {
        return policyEnforcement.hasPermission(
            outcome.getPlan(), PermissionService.UPDATE_PLAN_ACTION);
    }
}
