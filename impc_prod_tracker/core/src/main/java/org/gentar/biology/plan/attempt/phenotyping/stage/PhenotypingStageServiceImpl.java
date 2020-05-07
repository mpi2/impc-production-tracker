package org.gentar.biology.plan.attempt.phenotyping.stage;

import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhenotypingStageServiceImpl implements PhenotypingStageService
{
    private PhenotypingStageStateMachineResolver phenotypingStageStateMachineResolver;
    private TransitionAvailabilityEvaluator transitionAvailabilityEvaluator;

    public PhenotypingStageServiceImpl(
        PhenotypingStageStateMachineResolver phenotypingStageStateMachineResolver,
        TransitionAvailabilityEvaluator transitionAvailabilityEvaluator)
    {
        this.phenotypingStageStateMachineResolver = phenotypingStageStateMachineResolver;
        this.transitionAvailabilityEvaluator = transitionAvailabilityEvaluator;
    }

    @Override
    public List<TransitionEvaluation> evaluateNextTransitions(PhenotypingStage phenotypingStage)
    {
        List<ProcessEvent> transitions =
            phenotypingStageStateMachineResolver.getAvailableTransitionsByEntityStatus(phenotypingStage);
        return transitionAvailabilityEvaluator.evaluate(transitions, phenotypingStage);
    }
}
