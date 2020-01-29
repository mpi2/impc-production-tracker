package org.gentar.statemachine;

/**
 * Interface to be implemented by the entity to be processed in the state machine.
 */
public interface ProcessData
{
    ProcessEvent getEvent();
}
