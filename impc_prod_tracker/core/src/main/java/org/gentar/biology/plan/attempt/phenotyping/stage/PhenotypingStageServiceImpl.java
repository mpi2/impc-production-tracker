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
    private PhenotypingStageRepository phenotypingStageRepository;

    public PhenotypingStageServiceImpl(
        PhenotypingStageStateMachineResolver phenotypingStageStateMachineResolver,
        TransitionAvailabilityEvaluator transitionAvailabilityEvaluator,
        PhenotypingStageRepository phenotypingStageRepository)
    {
        this.phenotypingStageStateMachineResolver = phenotypingStageStateMachineResolver;
        this.transitionAvailabilityEvaluator = transitionAvailabilityEvaluator;
        this.phenotypingStageRepository = phenotypingStageRepository;
    }

    @Override
    public List<TransitionEvaluation> evaluateNextTransitions(PhenotypingStage phenotypingStage)
    {
        List<ProcessEvent> transitions =
            phenotypingStageStateMachineResolver.getAvailableTransitionsByEntityStatus(phenotypingStage);
        return transitionAvailabilityEvaluator.evaluate(transitions, phenotypingStage);
    }

    @Override
    public ProcessEvent getProcessEventByName(PhenotypingStage phenotypingStage, String name)
    {
        return phenotypingStageStateMachineResolver.getProcessEventByActionName(
            phenotypingStage, name);
    }

    @Override
    public PhenotypingStage getPhenotypingStageById(Long id)
    {
        return phenotypingStageRepository.findById(id).orElse(null);
    }
}
