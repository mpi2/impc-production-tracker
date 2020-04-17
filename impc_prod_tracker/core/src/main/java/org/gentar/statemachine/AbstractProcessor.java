package org.gentar.statemachine;

import lombok.Data;
import org.gentar.exceptions.SystemOperationFailedException;

/**
 * Define the abstract methods for a processor: the logic to change state in a state machine.
 */
@Data
public abstract class AbstractProcessor implements Processor
{
    private StateSetter stateSetter;
    private static final String TRANSITION_ERROR =
        "Transition [%s] cannot be executed because it goes from status [%s] to [%s] but current" +
            " status in the entity [%s] is [%s]";

    protected AbstractProcessor(StateSetter stateSetter)
    {
        this.stateSetter = stateSetter;
    }

    @Override
    public ProcessData process(ProcessData entity)
    {
        checkEntityIsNotNull(entity);
        tryToExecuteTransition(entity);
        return entity;
    }

    private void checkEntityIsNotNull(ProcessData entity)
    {
        if (entity == null)
        {
            throw new SystemOperationFailedException(
                "State Machine cannot be executed", "Entity is null");
        }
    }

    private void tryToExecuteTransition(ProcessData entity)
    {
        if (canExecuteTransition(entity))
        {
            ProcessEvent processEvent = entity.getEvent();
            if (processEvent != null)
            {
                ProcessState endState = getValidatedEndState(entity, processEvent);
                String statusName = endState.getInternalName();
                stateSetter.setStatusByName(entity, statusName);
            }
        }
    }

    private ProcessState getValidatedEndState(ProcessData entity, ProcessEvent transition)
    {
        String currentStatusName = entity.getStatus().getName();
        String initialStatusTransitionName = transition.getInitialState().getInternalName();
        if (!currentStatusName.equals(initialStatusTransitionName))
        {
            throw new SystemOperationFailedException(
                "Invalid transition",
                String.format(
                    TRANSITION_ERROR,
                    transition.getName(),
                    transition.getInitialState().getInternalName(),
                    transition.getEndState().getInternalName(),
                    entity.getClass().getSimpleName(),
                    entity.getStatus().getName()
                    ));
        }
        return transition.getEndState();
    }

    /**
     * Each concrete processor has to define the logic to decide if a transition can be done or not.
     * @param entity Entity to be processed.
     * @return True if the transition can be executed. False if not.
     */
    protected abstract boolean canExecuteTransition(ProcessData entity);
}
