package org.gentar.statemachine;

import org.gentar.biology.status.Status;

/**
 * Interface to be implemented by the entity to be processed in the state machine.
 */
public interface ProcessData
{
    ProcessEvent getEvent();
    void setEvent(ProcessEvent processEvent);
    Status getStatus();
    void setStatus(Status status);
}
