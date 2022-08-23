package org.gentar.biology.colony;

import org.gentar.biology.colony.engine.ColonyEvent;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.StateMachineResolver;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ColonyStateMachineResolver implements StateMachineResolver
{
    @Override
    public ProcessEvent getProcessEventByActionName(ProcessData processData, String actionName)
    {
        List<ProcessEvent> allEvents = getProcessEventsByColony();
        for (ProcessEvent processEvent : allEvents)
        {
            if (processEvent.getName().equalsIgnoreCase(actionName))
                return processEvent;
        }
        return null;
    }

    private List<ProcessEvent> getProcessEventsByColony()
    {
        return ColonyEvent.getAllEvents();
    }

    @Override
    public List<ProcessEvent> getAvailableTransitionsByEntityStatus(ProcessData processData)
    {
        List<ProcessEvent> allTransitionsByPlanType = getAvailableEventsByStateName(
            getProcessEventsByColony(), processData.getProcessDataStatus().getName());
        return allTransitionsByPlanType;
    }
}
