package org.gentar.biology.plan.engine.breeding.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

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
        return canAbortPlan((Plan) entity);
    }

    private boolean canAbortPlan(Plan plan)
    {
        // Put here the validations before aborting a Breeding Plan.
        return true;
    }
}
