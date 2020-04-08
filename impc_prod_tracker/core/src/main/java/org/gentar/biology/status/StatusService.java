package org.gentar.biology.status;

/**
 * Allows to interact with the Status table.
 */
public interface StatusService
{
    /**
     * Get a status with a given name.
     * @param name Name of the status.
     * @return {@link Status} corresponding to the given name or null if not found.
     */
    Status getStatusByName(String name);
    /**
     * Get a status with a given name.
     * @param name Name of the status.
     * @return {@link Status} corresponding to the given name. Throws exception if not found.
     */
    Status getStatusByNameFailWhenNotFound(String name);
}
