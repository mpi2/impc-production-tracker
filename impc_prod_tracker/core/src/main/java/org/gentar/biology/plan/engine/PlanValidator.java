package org.gentar.biology.plan.engine;

import org.gentar.biology.colony.ColonyValidator;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypeChecker;
import org.gentar.biology.plan.attempt.AttemptTypeService;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.attempt.cre_allele_modification.CreAlleleModificationValidator;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptValidator;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypeAttemptValidator;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.project.engine.ProjectValidator;
import org.gentar.exceptions.CommonErrorMessages;
import org.gentar.exceptions.ForbiddenAccessException;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.security.abac.ResourceAccessChecker;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.security.permissions.Actions;
import org.gentar.security.permissions.Operations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.Plan;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that validates the date in a  {@link Plan}.
 */
@Component
public class PlanValidator
{
    private final CrisprAttemptValidator crisprAttemptValidator;
    private final AttemptTypeService attemptTypeService;
    private final ContextAwarePolicyEnforcement policyEnforcement;
    private final ResourceAccessChecker<Plan> resourceAccessChecker;
    private final ProjectValidator projectValidator;
    private final PhenotypeAttemptValidator phenotypeAttemptValidator;
    private final ColonyValidator colonyValidator;
    private final CreAlleleModificationValidator creAlleleModificationValidator;

    private static final String ATTEMPT_TYPE_PLAN_TYPE_INVALID_ASSOCIATION =
        "The attempt type [%s] cannot be associated with a plan with type [%s].";
    private static final String NULL_OBJECT_ERROR = "%s cannot be null.";

    public PlanValidator(
            CrisprAttemptValidator crisprAttemptValidator,
            AttemptTypeService attemptTypeService,
            ContextAwarePolicyEnforcement policyEnforcement,
            ResourceAccessChecker<Plan> resourceAccessChecker,
            ProjectValidator projectValidator,
            PhenotypeAttemptValidator phenotypeAttemptValidator,
            ColonyValidator colonyValidator, CreAlleleModificationValidator creAlleleModificationValidator)
    {
        this.crisprAttemptValidator = crisprAttemptValidator;
        this.attemptTypeService = attemptTypeService;
        this.policyEnforcement = policyEnforcement;
        this.resourceAccessChecker = resourceAccessChecker;
        this.projectValidator = projectValidator;
        this.phenotypeAttemptValidator = phenotypeAttemptValidator;
        this.colonyValidator = colonyValidator;
        this.creAlleleModificationValidator = creAlleleModificationValidator;
    }

    /**
     * Validates the data in a plan
     *
     * @param plan Plan to be validated.
     */
    public void validate(Plan plan)
    {
        validateBasicPlanData(plan);
    }

    public void validateDataCreation(Plan plan)
    {
        if (AttemptTypeChecker.CRISPR_TYPE.equalsIgnoreCase(AttemptTypeChecker.getAttemptTypeName(plan)))
        {
            // Checks to Crispr attempt type.
        }
        if (AttemptTypeChecker.ES_CELL_TYPE.equalsIgnoreCase(AttemptTypeChecker.getAttemptTypeName(plan)))
        {

        }
        if (AttemptTypeChecker.CRE_ALLELE_MODIFICATION_TYPE.equalsIgnoreCase(AttemptTypeChecker.getAttemptTypeName(plan)))
        {
            Set<PlanStartingPoint> planStartingPoints = plan.getPlanStartingPoints();
            if (planStartingPoints != null)
            {
                planStartingPoints.forEach(x -> colonyValidator.validateDataForStartingPoint(x.getOutcome().getColony()));
            } else {
                throw new UserOperationFailedException(String.format(NULL_OBJECT_ERROR, "Starting Point (outcome)"));
            }
        }
        if (AttemptTypeChecker.PHENOTYPING_TYPE.equalsIgnoreCase(AttemptTypeChecker.getAttemptTypeName(plan)))
        {
            Set<PlanStartingPoint> planStartingPoints = plan.getPlanStartingPoints();
            if (planStartingPoints != null) {
                planStartingPoints.forEach(x -> colonyValidator.validateDataForStartingPoint(x.getOutcome().getColony()));
            } else {
                throw new UserOperationFailedException(String.format(NULL_OBJECT_ERROR, "Starting Point (outcome)"));
            }
        }
    }

    public void validateUpdate(Plan originalPlan, Plan plan)
    {
        validateAttemptData(originalPlan, plan);
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
            throw new UserOperationFailedException(
                String.format(CommonErrorMessages.NULL_FIELD_ERROR, "typeName"));
        }
    }

    private void validateAttemptTypeNotNull(Plan plan)
    {
        AttemptType attemptType = plan.getAttemptType();
        if (attemptType == null)
        {
            throw new UserOperationFailedException(
                String.format(CommonErrorMessages.NULL_FIELD_ERROR, "attemptTypeName"));
        }
    }

    private void validateAttemptData(Plan originalPlan, Plan plan)
    {
        if (AttemptTypeChecker.CRISPR_TYPE.equalsIgnoreCase(AttemptTypeChecker.getAttemptTypeName(plan)))
        {
            crisprAttemptValidator.validate(plan.getCrisprAttempt());
        }
        if (AttemptTypeChecker.ES_CELL_TYPE.equalsIgnoreCase(AttemptTypeChecker.getAttemptTypeName(plan)))
        {

        }
        if (AttemptTypeChecker.CRE_ALLELE_MODIFICATION_TYPE.equalsIgnoreCase(AttemptTypeChecker.getAttemptTypeName(plan)))
        {
            creAlleleModificationValidator.validateModificationExternalRefNotNull(plan.getCreAlleleModificationAttempt());
        }
        if (AttemptTypeChecker.PHENOTYPING_TYPE.equalsIgnoreCase(AttemptTypeChecker.getAttemptTypeName(plan)))
        {
            phenotypeAttemptValidator.validateStrainNotNull(plan.getPhenotypingAttempt());
            phenotypeAttemptValidator.validatePhenotypingExternalRefNotNull(plan.getPhenotypingAttempt());

            // If the originalAttempt already exists.
            if (originalPlan.getPhenotypingAttempt() != null)
            {
                phenotypeAttemptValidator.validateDataIfPhenotypeStageExists(originalPlan.getPhenotypingAttempt(),
                        plan.getPhenotypingAttempt());
            }
        }
    }

    /**
     * Check if the current logged user has permission to update the plan p.
     * @param plan Plan being updated.
     */
    public void validatePermissionToCreatePlan(Plan plan)
    {
        String planTypeName = plan.getPlanType().getName();
        if (PlanTypeName.PRODUCTION.getLabel().equals(planTypeName))
        {
            projectValidator.validatePermissionToAddProductionPlan(plan.getProject());
        }
        else if (PlanTypeName.PHENOTYPING.getLabel().equals(planTypeName))
        {
            projectValidator.validatePermissionToAddPhenotypingPlan(plan.getProject());
        }
    }

    /**
     * Check if the current logged user has permission to update the plan p.
     * @param plan Plan being updated.
     */
    public void validatePermissionToUpdatePlan(Plan plan)
    {
        if (!policyEnforcement.hasPermission(plan, Actions.UPDATE_PLAN_ACTION))
        {
            throwPermissionExceptionForPlan(Operations.UPDATE, plan);
        }
    }

    private void throwPermissionExceptionForPlan(Operations action, Plan plan)
    {
        String entityType = Plan.class.getSimpleName();
        throw new ForbiddenAccessException(action, entityType, plan.getPin());
    }

    public List<Plan> getCheckedCollection(Collection<Plan> plans)
    {
        return plans.stream().map(this::getAccessChecked)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public Plan getAccessChecked(Plan plan)
    {
        return (Plan) resourceAccessChecker.checkAccess(plan, Actions.READ_PLAN_ACTION);
    }

    public void validateReadPermissions(Plan plan)
    {
        try
        {
            policyEnforcement.checkPermission(plan, Actions.READ_PLAN_ACTION);
        }
        catch (AccessDeniedException ade)
        {
            throw new ForbiddenAccessException(
                Operations.READ, Plan.class.getSimpleName(), plan.getPin());
        }
    }

}
