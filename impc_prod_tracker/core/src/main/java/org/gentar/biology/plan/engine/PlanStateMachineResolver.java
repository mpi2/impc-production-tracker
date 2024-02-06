package org.gentar.biology.plan.engine;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptTypesName;
import org.gentar.biology.plan.engine.breeding.BreedingPlanEvent;
import org.gentar.biology.plan.engine.crispr.CrisprProductionPlanEvent;
import org.gentar.biology.plan.engine.crispr.HaploessentialProductionPlanEvent;
import org.gentar.biology.plan.engine.crispr_allele_modification.CrisprAlleleModificationPlanEvent;
import org.gentar.biology.plan.engine.es_cell.EsCellProductionPlanEvent;
import org.gentar.biology.plan.engine.es_cell_allele_modification.EsCellAlleleModificationPlanEvent;
import org.gentar.biology.plan.engine.phenotyping.PhenotypePlanEvent;
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
        return getAvailableEventsByStateName(
            getProcessEventsByPlan((Plan)processData), processData.getProcessDataStatus().getName());
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
        processEvents = switch (attemptType) {
            case CRISPR -> CrisprProductionPlanEvent.getAllEvents();
            case HAPLOESSENTIAL_CRISPR -> HaploessentialProductionPlanEvent.getAllEvents();
            case ADULT_PHENOTYPING, HAPLOESSENTIAL_PHENOTYPING -> PhenotypePlanEvent.getAllEvents();
            case BREEDING -> BreedingPlanEvent.getAllEvents();
            case ES_CELL_ALLELE_MODIFICATION -> EsCellAlleleModificationPlanEvent.getAllEvents();
            case CRISPR_ALLELE_MODIFICATION -> CrisprAlleleModificationPlanEvent.getAllEvents();
            case ES_CELL -> EsCellProductionPlanEvent.getAllEvents();
        };
        return processEvents;
    }
}
