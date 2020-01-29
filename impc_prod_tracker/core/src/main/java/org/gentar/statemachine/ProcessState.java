package org.gentar.statemachine;

import java.util.List;

/**
 * Represent a state in the statemachine
 */
public interface ProcessState
{
    String getName();
    List<ProcessEvent> getUserAvailableEvents();
    List<ProcessEvent> getSystemAvailableEvents();
}
