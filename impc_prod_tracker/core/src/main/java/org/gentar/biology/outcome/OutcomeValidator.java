package org.gentar.biology.outcome;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.ColonyValidator;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.specimen.Specimen;
import org.gentar.exceptions.CommonErrorMessages;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.gentar.security.permissions.Operations;
import org.springframework.stereotype.Component;

@Component
public class OutcomeValidator
{
    private final ContextAwarePolicyEnforcement policyEnforcement;
    private static final String NULL_FIELD_ERROR = "[%s] cannot be null.";
    private static final String CANNOT_READ_PLAN = "The outcome is linked to the plan %s and you " +
            "do not have permission to read it.";

    private final ColonyValidator colonyValidator;

    public OutcomeValidator(ContextAwarePolicyEnforcement policyEnforcement,
                            ColonyValidator colonyValidator)
    {
        this.policyEnforcement = policyEnforcement;
        this.colonyValidator = colonyValidator;
    }

    public void validateData(Outcome outcome)
    {
        OutcomeType outcomeType = outcome.getOutcomeType();
        if (outcomeType == null)
        {
            throw new UserOperationFailedException(String.format(NULL_FIELD_ERROR, "Outcome type"));
        }
        OutcomeTypeName outcomeTypeName = OutcomeTypeName.valueOfLabel(outcomeType.getName());

        if (outcomeTypeName.equals(OutcomeTypeName.COLONY))
        {
            validateColonyData(outcome.getColony());
        }
        else if (outcomeTypeName.equals(OutcomeTypeName.SPECIMEN))
        {
            validateSpecimentData(outcome.getSpecimen());
        }
    }

    public void validateUpdateData(Outcome originalOutcome, Outcome outcome)
    {
        OutcomeType outcomeType = outcome.getOutcomeType();
        OutcomeTypeName outcomeTypeName = OutcomeTypeName.valueOfLabel(outcomeType.getName());

        if (outcomeTypeName.equals(OutcomeTypeName.COLONY))
        {
            validateUpdateColonyData(originalOutcome.getColony(), outcome.getColony());
        }
    }

    private void validateColonyData(Colony colony)
    {
        colonyValidator.validateData(colony);
    }

    private void validateUpdateColonyData(Colony originalColony, Colony colony)
    {
        colonyValidator.validateUpdateData(originalColony, colony);
    }

    private void validateSpecimentData(Specimen specimen)
    {

    }

    // To create/edit an outcome the user needs to have permission to edit a plan
    public void validateCreationPermission(Outcome outcome)
    {
        if (!canEditPlan(outcome))
        {
            String detail = String.format(CommonErrorMessages.EDIT_PLAN_ERROR, outcome.getPlan().getPin());
            throwPermissionExceptionForOutcome(Operations.CREATE, outcome, detail);
        }
    }

    public void validateUpdatePermission(Outcome outcome)
    {
        if (!canEditPlan(outcome))
        {
            String detail = String.format(CommonErrorMessages.EDIT_PLAN_ERROR, outcome.getPlan().getPin());
            throwPermissionExceptionForOutcome(Operations.UPDATE, outcome, detail);
        }
    }

    private boolean canEditPlan(Outcome outcome)
    {
        return policyEnforcement.hasPermission(
            outcome.getPlan(), Actions.UPDATE_PLAN_ACTION);
    }

    private void throwPermissionExceptionForOutcome(Operations action, Outcome outcome, String detail)
    {
        String entityType = Outcome.class.getSimpleName();
        throw new ForbiddenAccessException(action, entityType, outcome.getTpo(), detail);
    }

    public void validateReadPermissions(Outcome outcome)
    {
        Plan plan = outcome.getPlan();
        if (!policyEnforcement.hasPermission(plan, Actions.READ_PLAN_ACTION))
        {
            String detail = String.format(CANNOT_READ_PLAN, plan.getPin());
            throwPermissionException(Operations.READ, outcome, detail);
        }
    }

    private void throwPermissionException(Operations action, Outcome outcome, String details)
    {
        String entityType = Outcome.class.getSimpleName();
        throw new ForbiddenAccessException(action, entityType, outcome.getTpo(), details);
    }
}
