package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

/**
 * Class with the logic to move a Colony to the state "Colony Aborted"
 */
@Component
public class ColonyAbortProcessor extends AbstractProcessor
{
    public ColonyAbortProcessor(ColonyStateSetter colonyStateSetter)
    {
        super(colonyStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        return canAbortColony((Colony) entity);
    }

    private boolean canAbortColony(Colony colony)
    {
        // Put here the validations before aborting a Colony.
        return true;
    }
}
