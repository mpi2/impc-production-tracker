package org.gentar.biology.plan.engine.crispr.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class AbortGltProcessor extends AbstractProcessor
{
    public AbortGltProcessor(PlanStateSetter planStateSetter)
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
        List<Colony> colonies = getColoniesByPlan(plan);
        if (!colonies.isEmpty())
        {
            result = colonies.stream().allMatch(x -> x.getStatus().getIsAbortionStatus());
        }
        return result;
    }

    private List<Colony> getColoniesByPlan(Plan plan)
    {
        List<Colony> colonies = new ArrayList<>();
        Set<Outcome> outcomes = plan.getOutcomes();
        if (outcomes != null)
        {
            outcomes.forEach(x -> {
                if (x.getColony() != null)
                {
                    colonies.add(x.getColony());
                }
            });
        }
        return colonies;
    }
}
