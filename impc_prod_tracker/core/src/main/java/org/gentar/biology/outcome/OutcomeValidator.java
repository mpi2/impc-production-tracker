package org.gentar.biology.outcome;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.gentar.biology.specimen.Specimen;
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
