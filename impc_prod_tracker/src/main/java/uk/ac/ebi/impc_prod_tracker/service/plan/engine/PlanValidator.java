package uk.ac.ebi.impc_prod_tracker.service.plan.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;

/**
 * Class that validates the date in a  {@link Plan}.
 */
@Component
public class PlanValidator
{
    /**
     * Validates the data in a plan
     * @param plan
     */
    public void validatePlan(Plan plan)
    {
        System.out.println("Validating Plan");
        validateBasicPlanData(plan);
        validateCrisprAttemptData(plan.getCrisprAttempt());
    }

    private void validateBasicPlanData(Plan plan)
    {

    }

    private void validateCrisprAttemptData(CrisprAttempt crisprAttempt)
    {
        if (crisprAttempt != null)
        {
            // Validates data in the crispr attempt.
        }
    }
}
