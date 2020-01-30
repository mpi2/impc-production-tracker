package org.gentar.statemachine;

/**
 * Executes the business rules for the corresponding process state transition step.
 */
public interface Processor
{
    ProcessData process(ProcessData data);
}
