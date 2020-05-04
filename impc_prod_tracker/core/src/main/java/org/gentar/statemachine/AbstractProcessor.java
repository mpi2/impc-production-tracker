package org.gentar.statemachine;

import lombok.Data;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.exceptions.UserOperationFailedException;

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

    @Override
    public abstract TransitionEvaluation evaluateTransition(
        ProcessEvent transition, ProcessData data);

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

        ProcessEvent processEvent = entity.getEvent();
        if (processEvent != null)
        {
            validateTransitionCanBeExecuted(processEvent, entity);
            ProcessState endState = getValidatedEndState(entity, processEvent);
            String statusName = endState.getInternalName();
            stateSetter.setStatusByName(entity, statusName);
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

    private void validateTransitionCanBeExecuted(ProcessEvent transition, ProcessData entity)
    {
        TransitionEvaluation transitionEvaluation = evaluateTransition(transition, entity);
        if (!transitionEvaluation.isExecutable())
        {
            throw new UserOperationFailedException(
                "Transition cannot be executed", transitionEvaluation.getNote());
        }
    }
}
