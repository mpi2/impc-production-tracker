package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageStateSetter;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class RollbackToAllDataProcessedProcessor extends AbstractProcessor
{
    private final ContextAwarePolicyEnforcement policyEnforcement;

    public RollbackToAllDataProcessedProcessor(
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
                "to 'Phenotyping All Data Processed'");
        }
        return transitionEvaluation;
    }

    private boolean canExecuteTransition()
    {
        return policyEnforcement.hasPermission(
            null, "REVERSE_FROM_PHENOTYPING_FINISHED_TO_ALL_DATA_PROCESSED");
    }
}
