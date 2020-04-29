package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

@Component
public class ConfirmGenotypeProcessor extends AbstractProcessor
{
    public ConfirmGenotypeProcessor(ColonyStateSetter colonyStateSetter)
    {
        super(colonyStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        //TODO: Validates a sequence must have been uploaded and the allele validated
        return true;
    }
}
