package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

/**
 * Processor to execute actions in a colony that don't require extra validations.
 */
@Component
public class ColonyProcessorWithoutValidations extends AbstractProcessor
{

    public ColonyProcessorWithoutValidations(ColonyStateSetter colonyStateSetter)
    {
        super(colonyStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean canExecuteTransition = canExecuteTransition((Colony)data);
        transitionEvaluation.setExecutable(canExecuteTransition);
        if (!canExecuteTransition)
        {
            transitionEvaluation.setNote("Transition cannot be executed");
        }
        return transitionEvaluation;
    }

    public boolean canExecuteTransition(Colony colony)
    {
        return true;
    }
}
