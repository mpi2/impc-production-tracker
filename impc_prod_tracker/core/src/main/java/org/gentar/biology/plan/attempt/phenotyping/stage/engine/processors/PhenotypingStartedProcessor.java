package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageStateSetter;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

/**
 * Class with the logic to check if a transition that moves the phenotyping stage to
 * 'Phenotyping Started' can be executed.
 */
@Component
public class PhenotypingStartedProcessor extends AbstractProcessor
{
    private final ContextAwarePolicyEnforcement policyEnforcement;

    public PhenotypingStartedProcessor(
        PhenotypingStageStateSetter phenotypingStageStateSetter,
        ContextAwarePolicyEnforcement policyEnforcement)
    {
        super(phenotypingStageStateSetter);
        this.policyEnforcement = policyEnforcement;
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean validTransition = canExecuteTransition();
        transitionEvaluation.setExecutable(validTransition);
        if (!validTransition)
        {
            transitionEvaluation.setNote("The current user does not have permission to move " +
                "to 'Phenotyping Started'");
        }
        return transitionEvaluation;
    }

    private boolean canExecuteTransition()
    {
        return policyEnforcement.hasPermission(null, "UPDATE_TO_PHENOTYPING_STARTED");
    }
}
