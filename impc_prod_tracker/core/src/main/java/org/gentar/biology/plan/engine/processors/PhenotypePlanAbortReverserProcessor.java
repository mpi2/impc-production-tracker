package org.gentar.biology.plan.engine.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

/**
 * Class with the logic to move a Phenotyping Plan back to "Phenotyping Production Registered" after being aborted.
 */
@Component
public class PhenotypePlanAbortReverserProcessor extends AbstractProcessor
{
    public PhenotypePlanAbortReverserProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        return canRevertAbortion((Plan) entity);
    }

    private boolean canRevertAbortion(Plan plan)
    {
        // Put here the needed validation before reverting an abortion.
        return true;
    }
}
