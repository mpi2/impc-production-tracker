package org.gentar.biology.plan.engine.breeding.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanQueryHelper;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Class with the logic to move a Breeding Plan to the state "Breeding Aborted"
 */
@Component
public class BreedingPlanAbortProcessor extends AbstractProcessor
{
    public BreedingPlanAbortProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        if (areAllColoniesAborted((Plan)entity))
        {
            return true;
        }
        throw new UserOperationFailedException(
            "Plan cannot be aborted",
            "The plan has colonies that are not aborted. Please abort them first");
    }

    private boolean areAllColoniesAborted(Plan plan)
    {
        boolean result = true;
        List<Colony> colonies = PlanQueryHelper.getColoniesByPlan(plan);
        if (!colonies.isEmpty())
        {
            result = colonies.stream().allMatch(x -> x.getStatus().getIsAbortionStatus());
        }
        return result;
    }
}
