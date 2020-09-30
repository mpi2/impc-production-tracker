package org.gentar.biology.mutation;

import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.gentar.security.permissions.Operations;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class MutationValidator
{
    private final ContextAwarePolicyEnforcement policyEnforcement;

    private static final String CANNOT_READ_PLAN = "The mutation is linked to the plan %s and you " +
        "do not have permission to read it.";

    public MutationValidator(ContextAwarePolicyEnforcement policyEnforcement)
    {
        this.policyEnforcement = policyEnforcement;
    }

    public void validateReadPermissions(Mutation mutation)
    {
        Set<Outcome> outcomes = mutation.getOutcomes();
        if (outcomes != null)
        {
            outcomes.forEach(outcome -> {
                Plan plan = outcome.getPlan();
                if (!policyEnforcement.hasPermission(plan, Actions.READ_PLAN_ACTION))
                {
                    String detail = String.format(CANNOT_READ_PLAN, plan.getPin());
                    throwPermissionException(Operations.READ, mutation, detail);
                }
            });
        }
    }

    private void throwPermissionException(Operations action, Mutation mutation, String details)
    {
        String entityType = Mutation.class.getSimpleName();
        throw new ForbiddenAccessException(action, entityType, mutation.getMin(), details);
    }
}
