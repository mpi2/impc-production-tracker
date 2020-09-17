package org.gentar.biology.plan.engine;

import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypeChecker;
import org.gentar.biology.plan.attempt.AttemptTypeService;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptValidator;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.Plan;
import java.util.List;

/**
 * Class that validates the date in a  {@link Plan}.
 */
@Component
public class PlanValidator
{
    private final CrisprAttemptValidator crisprAttemptValidator;
    private final AttemptTypeService attemptTypeService;

    private static final String ATTRIBUTE_NULL_ERROR = "The %s cannot be null.";
    private static final String ATTEMPT_TYPE_PLAN_TYPE_INVALID_ASSOCIATION =
        "The attempt type [%s] cannot be associated with a plan with type [%s].";

    public PlanValidator(
        CrisprAttemptValidator crisprAttemptValidator, AttemptTypeService attemptTypeService)
    {
        this.crisprAttemptValidator = crisprAttemptValidator;
        this.attemptTypeService = attemptTypeService;
    }

    /**
     * Validates the data in a plan
     * @param plan Plan to be validated.
     */
    public void validate(Plan plan)
    {
        validateBasicPlanData(plan);
        validateAttemptData(plan);
    }

    private void validateBasicPlanData(Plan plan)
    {
        validatePlanTypeNotNull(plan);
        validateAttemptTypeNotNull(plan);
        validateAttemptTypeByPlanType(plan);
    }

    private void validateAttemptTypeByPlanType(Plan plan)
    {
        PlanTypeName planTypeName = PlanTypeName.valueOfLabel(plan.getPlanType().getName());
        List<AttemptTypesName> validAttemptTypesNames =
            attemptTypeService.getAttemptTypesByPlanTypeName(planTypeName);
        AttemptTypesName attemptTypesName =
            AttemptTypesName.valueOfLabel(plan.getAttemptType().getName());
        if (!validAttemptTypesNames.contains(attemptTypesName))
        {
            throw new UserOperationFailedException(
                String.format(
                    ATTEMPT_TYPE_PLAN_TYPE_INVALID_ASSOCIATION,
                    plan.getAttemptType().getName(),
                    plan.getPlanType().getName()));
        }
    }

    private void validatePlanTypeNotNull(Plan plan)
    {
        PlanType planType = plan.getPlanType();
        if (planType == null)
        {
            throw new UserOperationFailedException(String.format(ATTRIBUTE_NULL_ERROR, "plan type"));
        }
    }

    private void validateAttemptTypeNotNull(Plan plan)
    {
        AttemptType attemptType = plan.getAttemptType();
        if (attemptType == null)
        {
            throw new UserOperationFailedException(String.format(ATTRIBUTE_NULL_ERROR, "attempt type"));
        }
    }

    private void validateAttemptData(Plan plan)
    {
        if (AttemptTypeChecker.CRISPR_TYPE.equalsIgnoreCase(AttemptTypeChecker.getAttemptTypeName(plan)))
        {
            crisprAttemptValidator.validate(plan.getCrisprAttempt());
        }
    }

}
