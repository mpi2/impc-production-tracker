package org.gentar.biology.colony;

import org.gentar.security.permissions.PermissionService;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionAvailabilityEvaluator;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ColonyServiceImpl implements ColonyService {


    private final ColonyRepository colonyRepository;
    private final ColonyStateMachineResolver colonyStateMachineResolver;
    private final TransitionAvailabilityEvaluator transitionAvailabilityEvaluator;

    public ColonyServiceImpl(ColonyRepository colonyRepository, ColonyStateMachineResolver colonyStateMachineResolver,
            TransitionAvailabilityEvaluator transitionAvailabilityEvaluator) {

        this.colonyRepository = colonyRepository;
        this.colonyStateMachineResolver = colonyStateMachineResolver;
        this.transitionAvailabilityEvaluator = transitionAvailabilityEvaluator;
    }

    @Override
    public List<TransitionEvaluation> evaluateNextTransitions(Colony colony) {
        List<ProcessEvent> transitions =
                colonyStateMachineResolver.getAvailableTransitionsByEntityStatus(colony);
        return transitionAvailabilityEvaluator.evaluate(transitions, colony);
    }

    @Override
    public ProcessEvent getProcessEventByName(Colony colony, String name) {
        return colonyStateMachineResolver.getProcessEventByActionName(colony, name);
    }

    @Override
    public Colony getColonyByColonyName(String name) {
        return colonyRepository.findByNameAndLegacyModificationIsFalseIgnoreCase(name);

    }


}
