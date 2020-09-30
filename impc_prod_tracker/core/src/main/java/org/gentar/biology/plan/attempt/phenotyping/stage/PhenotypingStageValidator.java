package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.exceptions.CommonErrorMessages;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.gentar.security.permissions.Operations;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhenotypingStageValidator
{
    private final PhenotypingStageRepository phenotypingStageRepository;
    private final ContextAwarePolicyEnforcement policyEnforcement;

    public PhenotypingStageValidator(
        PhenotypingStageRepository phenotypingStageRepository,
        ContextAwarePolicyEnforcement policyEnforcement)
    {
        this.phenotypingStageRepository = phenotypingStageRepository;
        this.policyEnforcement = policyEnforcement;
    }

    /**
     * Validates that a given phenotypingStage has valid data to be created/updated.
     * @param phenotypingStage The PhenotypingStage object.
     */
    public void validateData(PhenotypingStage phenotypingStage)
    {
        PhenotypingAttempt phenotypingAttempt = phenotypingStage.getPhenotypingAttempt();
        if (phenotypingAttempt == null)
        {
            throw new UserOperationFailedException(
                "The phenotyping stage requires a phenotyping attempt.");
        }
        PhenotypingStageType phenotypingStageType = phenotypingStage.getPhenotypingStageType();
        if (phenotypingStageType == null)
        {
            throw new UserOperationFailedException(
                "The phenotyping stage requires a phenotyping stage type.");
        }
        validateOnlyOneTypeByPhenotyping(phenotypingStage, phenotypingAttempt);
    }

    private void validateOnlyOneTypeByPhenotyping(
        PhenotypingStage phenotypingStage, PhenotypingAttempt phenotypingAttempt)
    {
        PhenotypingStageType phenotypingStageType = phenotypingStage.getPhenotypingStageType();
        List<PhenotypingStage> existingByType =
            phenotypingStageRepository.findAllByPhenotypingAttemptAndPhenotypingStageType(
                phenotypingAttempt, phenotypingStageType);
        // Remove the existing one from the count
        if (phenotypingStage.getId() != null)
        {
            existingByType = existingByType.stream()
                .filter(x -> !x.getId().equals(phenotypingStage.getId()))
                .collect(Collectors.toList());
        }
        if (existingByType != null && !existingByType.isEmpty())
        {
            throw new UserOperationFailedException(
                "A phenotyping stage of type [" + phenotypingStageType.getName() +
                    "] already exists for the plan " + phenotypingAttempt.getPlan().getPin() + ".");
        }
    }

    // To create/edit an phenotypingStage the user needs to have permission to edit a plan
    public void validateCreationPermission(PhenotypingStage phenotypingStage)
    {
        if (!canEditPlan(phenotypingStage))
        {
            String detail = String.format(
                CommonErrorMessages.EDIT_PLAN_ERROR, phenotypingStage.getPhenotypingAttempt().getPlan().getPin());
            throwPermissionExceptionForPhenotypingStage(Operations.CREATE, phenotypingStage, detail);
        }
    }

    public void validateUpdatePermission(PhenotypingStage phenotypingStage)
    {
        if (!canEditPlan(phenotypingStage))
        {
            String detail = String.format(
                CommonErrorMessages.EDIT_PLAN_ERROR, phenotypingStage.getPhenotypingAttempt().getPlan().getPin());
            throwPermissionExceptionForPhenotypingStage(Operations.UPDATE, phenotypingStage, detail);
        }
    }

    private boolean canEditPlan(PhenotypingStage phenotypingStage)
    {
        return policyEnforcement.hasPermission(
            phenotypingStage.getPhenotypingAttempt().getPlan(), Actions.UPDATE_PLAN_ACTION);
    }

    private void throwPermissionExceptionForPhenotypingStage(
        Operations action, PhenotypingStage phenotypingStage, String detail)
    {
        String entityType = "phenotyping stage";
        throw new ForbiddenAccessException(action, entityType, phenotypingStage.getPsn(), detail);
    }
}
