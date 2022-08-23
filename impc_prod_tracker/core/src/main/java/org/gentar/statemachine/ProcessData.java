package org.gentar.statemachine;

import org.gentar.biology.status.Status;

/**
 * Interface to be implemented by the entity to be processed in the state machine.
 */
public interface ProcessData
{
    ProcessEvent getProcessDataEvent();
    void setProcessDataEvent(ProcessEvent processEvent);
    Status getProcessDataStatus();
    void setProcessDataStatus(Status status);
}
