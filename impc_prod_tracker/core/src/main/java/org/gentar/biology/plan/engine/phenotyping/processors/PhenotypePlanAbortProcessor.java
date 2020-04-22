package org.gentar.biology.plan.engine.phenotyping.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanQueryHelper;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.status.Status;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Class with the logic to move a Phenotype Plan to the state "Phenotype Production Aborted"
 */
@Component
public class PhenotypePlanAbortProcessor extends AbstractProcessor
{
    public PhenotypePlanAbortProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        return canAbortPlan((Plan) entity);
    }

    private boolean canAbortPlan(Plan plan)
    {
        if (areAllPhenotypingStagesAborted(plan))
        {
            return true;
        }
        throw new UserOperationFailedException(
            "Plan cannot be aborted",
            "The plan has phenotyping stages that are not aborted. Please abort them first");
    }

    private boolean areAllPhenotypingStagesAborted(Plan plan)
    {
        boolean result = true;
        List<Status> phenotypingStagesStatuses = PlanQueryHelper.getPhenotypingStagesStatuses(plan);
        if (!phenotypingStagesStatuses.isEmpty())
        {
            result = phenotypingStagesStatuses.stream().allMatch(Status::getIsAbortionStatus);
        }
        return result;
    }
}
