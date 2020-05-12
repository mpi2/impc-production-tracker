package org.gentar.biology.specimen;

import org.gentar.biology.specimen.engine.SpecimenEvent;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.StateMachineResolver;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Class that resolves the state machine to use for a specimen.
 */
@Component
public class SpecimenStateMachineResolver implements StateMachineResolver
{
    @Override
    public ProcessEvent getProcessEventByActionName(ProcessData processData, String actionName)
    {
        List<ProcessEvent> allEvents = getProcessEventsBySpecimen();
        for (ProcessEvent processEvent : allEvents)
        {
            if (processEvent.getName().equalsIgnoreCase(actionName))
                return processEvent;
        }
        return null;
    }

    private List<ProcessEvent> getProcessEventsBySpecimen()
    {
        return SpecimenEvent.getAllEvents();
    }

    @Override
    public List<ProcessEvent> getAvailableTransitionsByEntityStatus(ProcessData processData)
    {
        return getAvailableEventsByStateName(
            getProcessEventsBySpecimen(), processData.getStatus().getName());
    }
}
