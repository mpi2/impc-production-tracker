package org.gentar.statemachine;


import java.util.List;

public interface StateMachineResolver
{
    /**
     * Given a processData and an action, this method returns the event (transition) in the
     * suitable state machine that can be later be executed on the entity.
     * @param processData Entity that is being evaluate.
     * @param actionName Action to execute on the plan.
     * @return a {@link ProcessEvent} object with the description of the transition to execute,
     * linked to a specific state machine.
     */
    ProcessEvent getProcessEventByActionName(ProcessData processData, String actionName);

    /**
     * Get the possible transitions for a entity. The type plan or attempt type define the state
     * machine and the current plan's status defines the available transitions from that state machine.
     * @param processData Entity being evaluated.
     * @return A list of {@link ProcessEvent} (transitions) that can be executed in the entity.
     */
    List<ProcessEvent> getAvailableTransitionsByEntityStatus(ProcessData processData);
}
