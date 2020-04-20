package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

@Component
public class ColonyProcessor extends AbstractProcessor
{

    public ColonyProcessor(ColonyStateSetter colonyStateSetter)
    {
        super(colonyStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        // Is there any special condition to check?
        return true;
    }
}
