package org.gentar.biology.plan.engine.crispr.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanQueryHelper;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.specimen.Specimen;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AbortEmbryosObtainedProcessor extends AbstractProcessor {

    // This class is designed to be used with haplo-essential crispr production plans

    public AbortEmbryosObtainedProcessor(PlanStateSetter planStateSetter) {
        super(planStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        if (areAllSpecimensAborted((Plan)entity))
        {
            return true;
        }
        throw new UserOperationFailedException(
                "Plan cannot be aborted",
                "The plan has specimens that are not aborted. Please abort them first");
    }

    private boolean areAllSpecimensAborted(Plan plan)
    {
        boolean result = true;
        List<Specimen> specimens = PlanQueryHelper.getSpecimensByPlan(plan);
        if (!specimens.isEmpty())
        {
            result = specimens.stream().allMatch(x -> x.getStatus().getIsAbortionStatus());
        }
        return result;
    }
}
