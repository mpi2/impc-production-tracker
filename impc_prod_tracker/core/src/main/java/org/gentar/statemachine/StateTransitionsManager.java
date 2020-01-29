package org.gentar.statemachine;

/**
 * Events handler. If the event is a pre-event then forwards the processing to a processor,
 * or if the event is a post-event, then it changes the state to the final state.
 * It also sets a default state if needed.
 */
public interface StateTransitionsManager
{
    public ProcessData processEvent(ProcessData data) throws ProcessException;
}
