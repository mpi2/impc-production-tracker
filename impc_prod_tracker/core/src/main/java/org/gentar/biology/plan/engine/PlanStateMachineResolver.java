package org.gentar.biology.plan.engine;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptTypes;
import org.gentar.biology.plan.engine.events.BreedingPlanEvent;
import org.gentar.biology.plan.engine.events.CrisprProductionPlanEvent;
import org.gentar.biology.plan.engine.events.LateAdultPhenotypePlanEvent;
import org.gentar.biology.plan.engine.events.PhenotypePlanEvent;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.statemachine.ProcessEvent;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the different state machines that a plan can have
 */
@Component
public class PlanStateMachineResolver
{
    /**
     * Given a plan and an action, this method returns the event (transition) in the suitable state
     * machine that can be later be executed on the project.
     * @param plan Plan that is being evaluate.
     * @param actionName Action to execute on the plan.
     * @return a {@link ProcessEvent} object with the description of the transition to execute,
     * linked to a specific state machine.
     */
    public ProcessEvent getProcessEventByActionName(Plan plan, String actionName)
    {
        List<ProcessEvent> allEvents = getProcessEventsByPlan(plan);
        for (ProcessEvent processEvent : allEvents)
        {
            if (processEvent.getName().equalsIgnoreCase(actionName))
                return processEvent;
        }
        return null;
    }

    /**
     * Get the possible transitions for a plan. The type plan or attempt type define the state
     * machine and the current plan's status defines the available transitions from that state machine.
     * @param plan Plan being evaluated.
     * @return A list of {@link ProcessEvent} (transitions) that can be executed in the plan.
     */
    public List<ProcessEvent> getAvailableTransitionsByPlanStatus(Plan plan)
    {
        return getAvailableEventsByStateName(
            getProcessEventsByPlan(plan), plan.getStatus().getName());
    }

    private List<ProcessEvent> getAvailableEventsByStateName(
        List<ProcessEvent> processEvents, String processStateName)
    {
        List<ProcessEvent> allEvents = new ArrayList<>();
        for (ProcessEvent processEventValue : processEvents)
        {
            if (processEventValue.getInitialState().getInternalName().equals(processStateName))
            {
                allEvents.add(processEventValue);
            }
        }
        return allEvents;
    }

    /**
     * Gets a list of {@link ProcessEvent} representing the state machine that is valid for a
     * plan given its plan type and attempt type.
     * @param plan Plan that is being checked.
     * @return A list of {@link ProcessEvent} representing the different transitions (state machine)
     * that can potentially be applied in the plan.
     */
    private List<ProcessEvent> getProcessEventsByPlan(Plan plan)
    {
        List<ProcessEvent> processEvents;
        AttemptTypes attemptType =
            AttemptTypes.getAttemptTypeEnumByName(plan.getAttemptType().getName());
        switch (attemptType)
        {
            case CRISPR:
                processEvents = CrisprProductionPlanEvent.getAllEvents();
                break;
            case EARLY_ADULT:
                processEvents = PhenotypePlanEvent.getAllEvents();
                break;
            case LATE_ADULT:
                processEvents = LateAdultPhenotypePlanEvent.getAllEvents();
                break;
            case BREEDING:
                processEvents = BreedingPlanEvent.getAllEvents();
                break;
            default:
                throw new SystemOperationFailedException(
                    "State machine cannot be resolved", "plan: " + plan.toString());
        }
        return processEvents;
    }
}
