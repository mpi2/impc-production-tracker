package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

@Component
public class ReverseGenotypeConfirmationProcessor extends AbstractProcessor
{
    public ReverseGenotypeConfirmationProcessor(ColonyStateSetter colonyStateSetter)
    {
        super(colonyStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        // TODO: Can be done only by CDA or DCC
        return false;
    }
}
