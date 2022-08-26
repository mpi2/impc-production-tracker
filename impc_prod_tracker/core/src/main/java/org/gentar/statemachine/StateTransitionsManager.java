package org.gentar.statemachine;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Events handler. If the event is a pre-event then forwards the processing to a processor,
 * or if the event is a post-event, then it changes the state to the final state.
 * It also sets a default state if needed.
 */
@Component
public class StateTransitionsManager
{
    private final ApplicationContext context;

    protected StateTransitionsManager(ApplicationContext context)
    {
        this.context = context;
    }

    /**
     * Executes a transition in an entity.
     * @param data Entity where the transition is going to be executed.
     * @return Entity after the transition is executed.
     */
    public ProcessData processEvent(ProcessData data)
    {
        return processStateTransition(data);
    }

    /**
     * The transition logic can be defined here because the process is the same for all the
     * machines: get the processor and if it is not null, process the data with the processor logic.
     * @param data The object in the state machine.
     * @return A {@link ProcessData} object where the transition was done.
     */
    private ProcessData processStateTransition(ProcessData data)
    {
        ProcessEvent processEvent = data.getProcessDataEvent();
        if (processEvent != null)
        {
            Class<? extends Processor> processor = processEvent.getNextStepProcessor();
            if (processor != null)
            {
                this.context.getBean(processor).process(data);
            }
        }
        return data;
    }

    /**
     * Evaluates a transition in an entity, indicating whether or not it can be executed and why.
     * @param processEvent Transition to be evaluated.
     * @param data Entity where the transition is going to be evaluated.
     * @return {@link TransitionEvaluation} object with the result of the evaluation.
     */
    public TransitionEvaluation evaluateTransition(ProcessEvent processEvent, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(processEvent);
        Class<? extends Processor> processor = processEvent.getNextStepProcessor();
        if (processor != null)
        {
            transitionEvaluation =
                this.context.getBean(processor).evaluateTransition(processEvent, data);
        }
        return transitionEvaluation;
    }
}
