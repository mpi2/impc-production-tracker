package org.gentar.statemachine;

/**
 * Represent a state in the statemachine.
 */
public interface ProcessState
{
    /**
     * Get the name of the state
     * @return String representing the name of the state.
     */
    String getName();

    /**
     * Get the name of the state that matches the value in the database (Status table i.e)
     * @return The exact name of the state in database
     */
    String getInternalName();
}
