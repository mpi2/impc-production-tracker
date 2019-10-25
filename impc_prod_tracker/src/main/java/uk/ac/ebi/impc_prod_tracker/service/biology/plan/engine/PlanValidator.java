package uk.ac.ebi.impc_prod_tracker.service.biology.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;

/**
 * Class that validates the date in a  {@link Plan}.
 */
@Component
public class PlanValidator
{
    private CrisprAttemptValidator crisprAttemptValidator;

    public PlanValidator(CrisprAttemptValidator crisprAttemptValidator)
    {
        this.crisprAttemptValidator = crisprAttemptValidator;
    }

    /**
     * Validates the data in a plan
     * @param plan Plan to be validated.
     */
    public void validate(Plan plan)
    {
        System.out.println("Validating Plan");
        validateBasicPlanData(plan);
        validateAttemptData(plan);
    }

    private void validateBasicPlanData(Plan plan)
    {
        System.out.println("validating basic data of the plan...");
    }

    private void validateAttemptData(Plan plan)
    {
        System.out.println("Validate attempt: "+ AttemptTypeChecker.getAttemptTypeName(plan));
        if (AttemptTypeChecker.CRISPR_TYPE.equalsIgnoreCase(AttemptTypeChecker.getAttemptTypeName(plan)))
        {
            crisprAttemptValidator.validate(plan.getCrisprAttempt());
        }
    }
}
