package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingStageStateSetter;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;

public class EarlyAdultPhenotypingAbortReverserProcessor extends AbstractProcessor
{
    public EarlyAdultPhenotypingAbortReverserProcessor(
        PhenotypingStageStateSetter phenotypingStageStateSetter)
    {
        super(phenotypingStageStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean canRevertAbortion = canRevertAbortion((PhenotypingStage) data);
        transitionEvaluation.setExecutable(canRevertAbortion);
        if (!canRevertAbortion)
        {
            transitionEvaluation.setNote("Phenotyping stage abortion cannot be reversed");
        }
        return transitionEvaluation;
    }

    private boolean canRevertAbortion(PhenotypingStage phenotypingStage)
    {
        // Put here the needed validation before reverting an abortion.
        return true;
    }
}
