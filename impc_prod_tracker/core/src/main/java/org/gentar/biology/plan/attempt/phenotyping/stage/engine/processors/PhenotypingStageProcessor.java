package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingStageStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

@Component
public class PhenotypingStageProcessor extends AbstractProcessor
{
    public PhenotypingStageProcessor(
        PhenotypingStageStateSetter phenotypingStageStateSetter)
    {
        super(phenotypingStageStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        return false;
    }
}

