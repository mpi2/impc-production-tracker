package org.gentar.biology.outcome;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.gentar.biology.specimen.Specimen;
import org.gentar.exceptions.CommonErrorMessages;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.gentar.security.permissions.PermissionService;
import org.springframework.stereotype.Component;

@Component
public class OutcomeValidator
{
    private final ContextAwarePolicyEnforcement policyEnforcement;
    private static final String NULL_FIELD_ERROR = "[%s] cannot be null.";

    private final ColonyValidator colonyValidator;

    public OutcomeValidator(ContextAwarePolicyEnforcement policyEnforcement, ColonyValidator colonyValidator)
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

    private void validateColonyData(Colony colony)
    {
        colonyValidator.validateData(colony);
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
            throwPermissionExceptionForOutcome(Actions.CREATE, outcome, detail);
        }
    }

    public void validateUpdatePermission(Outcome outcome)
    {
        if (!canEditPlan(outcome))
        {
            String detail = String.format(CommonErrorMessages.EDIT_PLAN_ERROR, outcome.getPlan().getPin());
            throwPermissionExceptionForOutcome(Actions.UPDATE, outcome, detail);
        }
    }

    private boolean canEditPlan(Outcome outcome)
    {
        return policyEnforcement.hasPermission(
            outcome.getPlan(), PermissionService.UPDATE_PLAN_ACTION);
    }

    private void throwPermissionExceptionForOutcome(Actions action, Outcome outcome, String detail)
    {
        String entityType = Outcome.class.getSimpleName();
        throw new ForbiddenAccessException(action, entityType, outcome.getTpo(), detail);
    }
}
