package org.gentar.statemachine;

import lombok.Setter;
import org.gentar.biology.status.Status;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class executes the transitions (events) that are triggered by the system.
 * When the entity to process has some specific data, the system can trigger a change in the state.
 * This class should be generic enough so it needs some classes to be set in order to manage
 * the logic of saving data in history.
 */
@Component
@Setter
public class SystemEventsExecutor
{
    private StateTransitionsManager stateTransitionManager;
    private static final String USER_ACTION_PRESENT_ERROR_MESSAGE =
        "Trying to execute action [%s] but also modifying data that causes a change in status. " +
            "Please do this in two different steps.";

    public SystemEventsExecutor(StateTransitionsManager stateTransitionManager)
    {
        this.stateTransitionManager = stateTransitionManager;
    }

    private StateMachineResolver stateMachineResolver;
    private ProcessEvent originalEvent;

    /**
     * Tries to execute as many system triggered transitions as possible for an entity.
     * System triggered transitions can only be executed if there is not a transition triggered
     * by the user at the same time. If that's the case a
     * {@link UserOperationFailedException} is thrown.
     * @param entity Entity to be processed.
     */

    public void execute(ProcessData entity)
    {
        originalEvent = entity.getProcessDataEvent();
        executeNextTransitions(entity);
        resetOriginalEvent(entity);
    }

    private void executeNextTransitions(ProcessData entity)
    {
        List<ProcessEvent> systemTransitions = getSystemTransitions(entity);
        for (ProcessEvent transition: systemTransitions)
        {
            boolean couldExecuteTransition = tryToExecuteTransition(transition, entity);
            if (couldExecuteTransition)
            {
                executeNextTransitions(entity);
                break;
            }
        }
    }

    private boolean tryToExecuteTransition(ProcessEvent transition, ProcessData entity)
    {
        boolean statusChanged = false;
        String statusNameBeforeTransition = entity.getProcessDataStatus().getName();
        entity.setProcessDataEvent(transition);
        TransitionEvaluation transitionEvaluation =
            stateTransitionManager.evaluateTransition(transition, entity);
        if (transitionEvaluation.isExecutable())
        {
            Status newStatus = stateTransitionManager.processEvent(entity).getProcessDataStatus();
            statusChanged = !statusNameBeforeTransition.equals(newStatus.getName());
            // Already set in processor, needed here because assignation is lost in tests
            entity.setProcessDataStatus(newStatus);
            if (statusChanged)
            {
                validateNoUserTriggeredTransitionIsPresent();
            }
        }

        return statusChanged;
    }

    private List<ProcessEvent> getSystemTransitions(ProcessData entity)
    {
        if (stateMachineResolver == null)
        {
            throw new SystemOperationFailedException(
                "System triggered transitions could not be executed",
                "No StateTransitionsManager provided");
        }
        var allPossibleTransitions =
            stateMachineResolver.getAvailableTransitionsByEntityStatus(entity);
        return allPossibleTransitions.stream()
            .filter(x -> !x.isTriggeredByUser())
            .collect(Collectors.toList());
    }

    private void validateNoUserTriggeredTransitionIsPresent()
    {
        if (originalEvent != null && originalEvent.isTriggeredByUser())
        {
            throw new UserOperationFailedException(
                String.format(USER_ACTION_PRESENT_ERROR_MESSAGE, originalEvent.getName()));
        }
    }

    // Set the original event the plan had before any system transition was performed.
    private void resetOriginalEvent(ProcessData entity)
    {
        entity.setProcessDataEvent(originalEvent);
    }
}
