package org.gentar.biology.plan.engine;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptTypes;
import org.gentar.biology.plan.engine.events.BreedingPlanEvent;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanEvent;
import org.gentar.biology.plan.engine.events.PhenotypePlanEvent;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.StateMachineResolver;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the different state machines that a plan can have
 */
@Component
public class PlanStateMachineResolver implements StateMachineResolver
{
    @Override
    public ProcessEvent getProcessEventByActionName(ProcessData processData, String actionName)
    {
        List<ProcessEvent> allEvents = getProcessEventsByPlan((Plan)processData);
        for (ProcessEvent processEvent : allEvents)
        {
            if (processEvent.getName().equalsIgnoreCase(actionName))
                return processEvent;
        }
        return null;
    }

    @Override
    public List<ProcessEvent> getAvailableTransitionsByEntityStatus(ProcessData processData)
    {
        return getAvailableEventsByStateName(
            getProcessEventsByPlan((Plan)processData), processData.getStatus().getName());
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
            case ADULT_PHENOTYPING: case HAPLOESSENTIAL_PHENOTYPING:
                processEvents = PhenotypePlanEvent.getAllEvents();
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
