package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

/**
 * Class to process transitions in a phenotyping stage machine that don't require any special
 * validation.
 */
@Component
public class PhenotypingStageProcessorWithoutValidations extends AbstractProcessor
{
    public PhenotypingStageProcessorWithoutValidations(PhenotypingStageStateSetter stageStateSetter)
    {
        super(stageStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        transitionEvaluation.setExecutable(true);
        return transitionEvaluation;
    }
}
