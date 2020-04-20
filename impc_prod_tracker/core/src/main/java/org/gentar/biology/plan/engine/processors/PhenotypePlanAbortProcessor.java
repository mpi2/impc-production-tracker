package org.gentar.biology.plan.engine.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

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
        // Put here the validations before aborting a Phenotyping Plan.
        return true;
    }
}
