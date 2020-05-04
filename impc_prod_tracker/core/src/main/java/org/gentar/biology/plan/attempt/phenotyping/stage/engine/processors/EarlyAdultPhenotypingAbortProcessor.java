package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingStageStateSetter;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class EarlyAdultPhenotypingAbortProcessor extends AbstractProcessor
{
    public EarlyAdultPhenotypingAbortProcessor(
        PhenotypingStageStateSetter phenotypingStageStateSetter)
    {
        super(phenotypingStageStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean canAbortPhenotypingStage = canAbortPhenotypingStage((PhenotypingStage) data);
        transitionEvaluation.setExecutable(canAbortPhenotypingStage);
        if (!canAbortPhenotypingStage)
        {
            transitionEvaluation.setNote("Phenotyping stage cannot be aborted");
        }
        return transitionEvaluation;
    }

    private boolean canAbortPhenotypingStage(PhenotypingStage phenotypingStage) {
        // Put here the validations before aborting a Phenotyping Stage.
        return true;
    }
}