package org.gentar.biology.colony;

import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ColonyServiceImpl implements ColonyService
{
    private ColonyStateMachineResolver colonyStateMachineResolver;
    private TransitionAvailabilityEvaluator transitionAvailabilityEvaluator;

    public ColonyServiceImpl(
        ColonyStateMachineResolver colonyStateMachineResolver,
        TransitionAvailabilityEvaluator transitionAvailabilityEvaluator)
    {
        this.colonyStateMachineResolver = colonyStateMachineResolver;
        this.transitionAvailabilityEvaluator = transitionAvailabilityEvaluator;
    }

    @Override
    public List<TransitionEvaluation> evaluateNextTransitions(Colony colony)
    {
        List<ProcessEvent> transitions =
            colonyStateMachineResolver.getAvailableTransitionsByEntityStatus(colony);
        return transitionAvailabilityEvaluator.evaluate(transitions, colony);
    }

    @Override
    public ProcessEvent getProcessEventByName(Colony colony, String name)
    {
        return colonyStateMachineResolver.getProcessEventByActionName(colony, name);
    }
}
