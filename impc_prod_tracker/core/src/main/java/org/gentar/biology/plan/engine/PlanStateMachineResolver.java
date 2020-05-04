package org.gentar.biology.plan.engine;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.engine.breeding.BreedingPlanEvent;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanEvent;
import org.gentar.biology.plan.engine.crispr.HaploessentialProductionPlanEvent;
import org.gentar.biology.plan.engine.phenotyping.PhenotypePlanEvent;
import org.gentar.exceptions.SystemOperationFailedException;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.StateMachineResolver;
import org.springframework.stereotype.Component;
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
        List<ProcessEvent> allTransitionsByPlanType = getAvailableEventsByStateName(
            getProcessEventsByPlan((Plan)processData), processData.getStatus().getName());
        return allTransitionsByPlanType;
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
        AttemptTypesName attemptType = AttemptTypesName.valueOfLabel(plan.getAttemptType().getName());
        switch (attemptType)
        {
            case CRISPR:
                processEvents = CrisprProductionPlanEvent.getAllEvents();
                break;
            case HAPLOESSENTIAL_CRISPR:
                processEvents = HaploessentialProductionPlanEvent.getAllEvents();
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
