package org.gentar.statemachine;

import org.gentar.biology.status.Status;

public interface StateSetter
{
    /**
     * Sets the status for an entity. It also record the stamp so there is historic information.
     * @param entity The entity to be updated.
     * @param status The new status.
     */
    void setStatus(ProcessData entity, Status status);
    /**
     * Sets the status for an entity. It also record the stamp so there is historic information.
     * @param entity The entity to be updated.
     * @param statusName A string with the name of the status.
     */
    void setStatusByName(ProcessData entity, String statusName);
}
