package org.gentar.biology.plan.engine.breeding.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class BreedingStartedProcessor extends AbstractProcessor
{
    public BreedingStartedProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean breedingAttemptExists = breedingAttemptExists((Plan) data);
        transitionEvaluation.setExecutable(breedingAttemptExists);
        if (!breedingAttemptExists)
        {
            transitionEvaluation.setNote(
                "The plan does not have a breeding plan yet");
        }
        return transitionEvaluation;
    }

    private boolean breedingAttemptExists(Plan plan)
    {
        return plan.getBreedingAttempt() != null;
    }
}
