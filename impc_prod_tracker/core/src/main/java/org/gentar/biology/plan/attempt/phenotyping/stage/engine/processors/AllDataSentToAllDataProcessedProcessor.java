package org.gentar.biology.plan.attempt.phenotyping.stage.engine.processors;

import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageStateSetter;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

/**
 * Logic to validate if the phenotyping stage can be moved from 'All Data Sent' to
 * 'All Data Processed'.
 */
@Component
public class AllDataSentToAllDataProcessedProcessor extends AbstractProcessor
{
    private ContextAwarePolicyEnforcement policyEnforcement;

    public AllDataSentToAllDataProcessedProcessor(
        PhenotypingStageStateSetter stageStateSetter, ContextAwarePolicyEnforcement policyEnforcement)
    {
        super(stageStateSetter);
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
            transitionEvaluation.setNote("The current user does not have permission to move " +
                "to 'All Data Processed'");
        }
        return transitionEvaluation;
    }

    private boolean canExecuteTransition()
    {
        return policyEnforcement.hasPermission(
            null, "UPDATE_FROM_ALL_DATA_SENT_TO_ALL_DATA_PROCESSED");
    }
}
