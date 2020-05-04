package org.gentar.biology.colony;

import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ColonyService
{
    private ColonyStateMachineResolver colonyStateMachineResolver;
    private TransitionAvailabilityEvaluator transitionAvailabilityEvaluator;

    public ColonyService(
        ColonyStateMachineResolver colonyStateMachineResolver,
        TransitionAvailabilityEvaluator transitionAvailabilityEvaluator)
    {
        this.colonyStateMachineResolver = colonyStateMachineResolver;
        this.transitionAvailabilityEvaluator = transitionAvailabilityEvaluator;
    }

    /**
     * Evaluates the transitions for a colony given its current status. To do that, this
     * method resolves the correct state machine for this plan and then checks what are the
     * possible transitions, evaluating each one and seeing if they could be executed
     * by the user or not.
     * @param colony Colony to evaluate.
     * @return The list of TransitionEvaluation that informs for each transition if it can
     * be executed or not, as long as a note explaining why in case it cannot be executed.
     */
    public List<TransitionEvaluation> evaluateNextTransitions(Colony colony)
    {
        List<ProcessEvent> transitions =
            colonyStateMachineResolver.getAvailableTransitionsByEntityStatus(colony);
        return transitionAvailabilityEvaluator.evaluate(transitions, colony);
    }
}
