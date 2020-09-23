package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageStateSetter;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class RollbackToPhenotypingRegisteredProcessor extends AbstractProcessor
{
    private ContextAwarePolicyEnforcement policyEnforcement;

    public RollbackToPhenotypingRegisteredProcessor(
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
        boolean canAbortPhenotypingStage = canExecuteTransition();
        transitionEvaluation.setExecutable(canAbortPhenotypingStage);
        if (!canAbortPhenotypingStage)
        {
            transitionEvaluation.setNote("The current user does not have permission to rollback " +
                    "to 'Phenotyping Registered'");
        }
        return transitionEvaluation;
    }

    private boolean canExecuteTransition()
    {
        return policyEnforcement.hasPermission(
                null, "REVERSE_FROM_PHENOTYPING_STARTED_TO_PHENOTYPING_REGISTERED");
    }
}
