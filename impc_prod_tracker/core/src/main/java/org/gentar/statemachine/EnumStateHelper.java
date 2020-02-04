package org.gentar.statemachine;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for Enums implementing {@link ProcessState} class.
 */
public class EnumStateHelper
{
    /**
     * Search the ProcessState in a list that has an internal name matching internalName.
     * @param processStates All the states to search in.
     * @param internalName The internal name of the state we are looking for.
     * @return Found state or null if not found.
     */
    public static ProcessState getStateByInternalName(
        List<ProcessState> processStates, String internalName)
    {
        for (ProcessState processStateValue : processStates)
        {
            if (processStateValue.getInternalName().equalsIgnoreCase(internalName))
                return processStateValue;
        }
        return null;
    }

    /**
     * Given a State, this method returns the possible events that can be executed next.
     * @param processEvents All the events in the machine.
     * @param processState The state we want to check
     * @return A subset of events in the machine that can be executed given the state.
     */
    public static List<ProcessEvent> getAvailableEventsByState(
        List<ProcessEvent> processEvents, ProcessState processState)
    {
        List<ProcessEvent> allEvents = new ArrayList<>();
        for (ProcessEvent processEventValue : processEvents)
        {
            if (processEventValue.getInitialState().equals(processState))
            {
                allEvents.add(processEventValue);
            }
        }
        return allEvents;
    }
}
