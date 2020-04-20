package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingStageStateSetter;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;

public class EarlyAdultPhenotypingAbortProcessor extends AbstractProcessor
{
    public EarlyAdultPhenotypingAbortProcessor(PhenotypingStageStateSetter phenotypingStageStateSetter)
    {
        super(phenotypingStageStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        return canAbortPhenotypingStage((PhenotypingStage)entity);
    }

    private boolean canAbortPhenotypingStage(PhenotypingStage phenotypingStage) {
        // Put here the validations before aborting a Phenotyping Stage.
        return true;
    }
}