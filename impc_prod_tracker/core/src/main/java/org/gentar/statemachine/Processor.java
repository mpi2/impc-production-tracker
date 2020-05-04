package org.gentar.statemachine;

/**
 * Executes the business rules for the corresponding process state transition step.
 */
public interface Processor
{
    /**
     * Executes the transition that the 'event' inside 'data' represents.
     * @param data Entity where the transition is going to be applied.
     * @return The entity after the transition is executed.
     * @Throws {@link org.gentar.exceptions.UserOperationFailedException} if the transition cannot
     * be executed.
     */
    ProcessData process(ProcessData data);

    /**
     * Evaluates if a transition in data can be executed or not.
     * @param data Entity in which we want to evaluate if the transition can or not be executed.
     * @return {@link TransitionEvaluation} object with information about the possibility or not
     * to execute the transition in the data entity.
     */
    TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data);
}
