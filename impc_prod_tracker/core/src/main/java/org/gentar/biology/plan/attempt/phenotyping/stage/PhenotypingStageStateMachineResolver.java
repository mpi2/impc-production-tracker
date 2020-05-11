package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.biology.plan.attempt.phenotyping.stage.engine.PhenotypingStageEvent;
import org.gentar.biology.plan.attempt.phenotyping.stage.engine.EarlyAdultEvent;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeName;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.StateMachineResolver;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * This class handles the different state machines that a phenotyping stage can have
 */
@Component
public class PhenotypingStageStateMachineResolver implements StateMachineResolver
{
    @Override
    public ProcessEvent getProcessEventByActionName(ProcessData processData, String actionName)
    {
        List<ProcessEvent> allEvents =
            getProcessEventsByPhenotypingStage((PhenotypingStage)processData);
        for (ProcessEvent processEvent : allEvents)
        {
            if (processEvent.getName().equalsIgnoreCase(actionName))
                return processEvent;
        }
        return null;
    }

    /**
     * Gets a list of {@link ProcessEvent} representing the state machine that is valid for a
     * phenotyping stage given its phenotyping stage type.
     * @param phenotypingStage PhenotypingStage that is being checked.
     * @return A list of {@link ProcessEvent} representing the different transitions (state machine)
     * that can potentially be applied in the phenotypingStage.
     */
    private List<ProcessEvent> getProcessEventsByPhenotypingStage(PhenotypingStage phenotypingStage)
    {
        List<ProcessEvent> processEvents;
        PhenotypingStageType phenotypingStageType = phenotypingStage.getPhenotypingStageType();
        if (PhenotypingStageTypeName.EARLY_ADULT.getLabel().equals(phenotypingStageType.getName()))
        {
            processEvents = EarlyAdultEvent.getAllEvents();
        }
        else
        {
            processEvents = PhenotypingStageEvent.getAllEvents();
        }
        return processEvents;
    }

    @Override
    public List<ProcessEvent> getAvailableTransitionsByEntityStatus(ProcessData processData)
    {
        List<ProcessEvent> allTransitionsByPlanType = getAvailableEventsByStateName(
            getProcessEventsByPhenotypingStage(
                (PhenotypingStage)processData), processData.getStatus().getName());
        return allTransitionsByPlanType;
    }
}
