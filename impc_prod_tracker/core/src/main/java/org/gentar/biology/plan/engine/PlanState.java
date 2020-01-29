package org.gentar.biology.plan.engine;

import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;

import java.util.ArrayList;
import java.util.List;

public enum PlanState implements ProcessState
{
    MicroInjectionInProgress("Micro-injection In Progress"),
    MicroInjectionComplete("Micro-injection Complete"),
    Aborted("Micro-injection Aborted");

    private String internalName;

    PlanState(String internalName)
    {
        this.internalName = internalName;
    }

    public String getInternalName()
    {
        return internalName;
    }

    public static PlanState getStateByInternalName(String internalName)
    {
        PlanState[] planStates = PlanState.values();
        for (PlanState planState : planStates)
        {
            if (planState.internalName.equalsIgnoreCase(internalName))
                return planState;
        }
        return null;
    }

    @Override
    public String getName()
    {
        return this.name();
    }

    public List<ProcessEvent> getAllAvailableEvents()
    {
        List<ProcessEvent> allEvents = new ArrayList<>();
        PlanEvent[] processEvents = PlanEvent.values();
        for (ProcessEvent processEvent : processEvents)
        {
            if (processEvent.getInitialState().equals(this))
            {
                allEvents.add(processEvent);
            }
        }
        return allEvents;
    }

    @Override
    public List<ProcessEvent> getUserAvailableEvents()
    {
        List<ProcessEvent> availableEvents = new ArrayList<>();
        PlanEvent[] processEvents = PlanEvent.values();
        for (ProcessEvent processEvent : processEvents)
        {
            if (processEvent.getInitialState().equals(this) && processEvent.isTriggeredByUser())
            {
                availableEvents.add(processEvent);
            }
        }
        return availableEvents;
    }

    @Override
    public List<ProcessEvent> getSystemAvailableEvents()
    {
        List<ProcessEvent> availableEvents = new ArrayList<>();
        PlanEvent[] processEvents = PlanEvent.values();
        for (ProcessEvent processEvent : processEvents)
        {
            if (processEvent.getInitialState().equals(this) && !processEvent.isTriggeredByUser())
            {
                availableEvents.add(processEvent);
            }
        }
        return availableEvents;
    }

    public static void main(String[] args)
    {
        PlanState[] planStates = PlanState.values();
        for (PlanState planState : planStates)
        {
            System.out.println(
                "Plan State: " + planState.getName() +
                 " with user available events:" + planState.getUserAvailableEvents() +
                 " with system available events:" + planState.getSystemAvailableEvents());
        }
    }
}
