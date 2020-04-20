package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingStageStateSetter;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;

public class EarlyAdultPhenotypingAbortReverserProcessor extends AbstractProcessor
{
    public EarlyAdultPhenotypingAbortReverserProcessor(
        PhenotypingStageStateSetter phenotypingStageStateSetter)
    {
        super(phenotypingStageStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        return canRevertAbortion((PhenotypingStage)entity);
    }

    private boolean canRevertAbortion(PhenotypingStage phenotypingStage)
    {
        // Put here the needed validation before reverting an abortion.
        return true;
    }
}
