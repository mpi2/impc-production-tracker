package org.gentar.biology.plan.engine.breeding.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

@Component
public class BreedingStartedProcessor extends AbstractProcessor
{
    public BreedingStartedProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        return breedingAttemptExists((Plan) entity);
    }

    private boolean breedingAttemptExists(Plan plan)
    {
        return plan.getBreedingAttempt() != null;
    }
}
