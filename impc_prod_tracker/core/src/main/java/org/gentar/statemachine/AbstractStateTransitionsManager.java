package org.gentar.statemachine;

/**
 * Enforces initialization of the state where needed.
 */
public abstract class AbstractStateTransitionsManager implements StateTransitionsManager
{
    protected abstract ProcessData initializeState(ProcessData data);
    protected abstract ProcessData processStateTransition(ProcessData data);

    @Override
    public ProcessData processEvent(ProcessData data)
    {
        initializeState(data);
        return processStateTransition(data);
    }
}
