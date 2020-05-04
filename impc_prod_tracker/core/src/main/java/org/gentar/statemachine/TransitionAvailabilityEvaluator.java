package org.gentar.statemachine;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransitionAvailabilityEvaluator
{
    private StateTransitionsManager stateTransitionsManager;

    public TransitionAvailabilityEvaluator(StateTransitionsManager stateTransitionsManager)
    {
        this.stateTransitionsManager = stateTransitionsManager;
    }

    public TransitionEvaluation evaluate(ProcessEvent transition, ProcessData entity)
    {
        return stateTransitionsManager.evaluateTransition(transition, entity);
    }

    public List<TransitionEvaluation> evaluate(List<ProcessEvent> transitions, ProcessData entity)
    {
        List<TransitionEvaluation> transitionEvaluations = new ArrayList<>();
        if (transitions != null)
        {
            transitions.forEach(x -> transitionEvaluations.add(evaluate(x, entity)));
        }
        return transitionEvaluations;
    }
}
