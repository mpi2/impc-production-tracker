package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.springframework.stereotype.Component;

/**
 * Class with the logic to move a Colony back to "Genotype Not Confirmed" after being aborted.
 */
@Component
public class ColonyAbortReverserProcessor extends AbstractProcessor
{
    public ColonyAbortReverserProcessor(ColonyStateSetter colonyStateSetter)
    {
        super(colonyStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        return canRevertAbortion((Colony) entity);
    }

    private boolean canRevertAbortion(Colony colony)
    {
        // Put here the needed validation before reverting an abortion.
        return true;
    }
}

