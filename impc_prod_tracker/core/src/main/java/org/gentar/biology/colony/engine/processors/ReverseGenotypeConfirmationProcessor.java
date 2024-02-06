package org.gentar.biology.colony.engine.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.colony.engine.ColonyStateSetter;
import org.gentar.security.abac.spring.ContextAwarePolicyEnforcement;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class ReverseGenotypeConfirmationProcessor extends AbstractProcessor
{
    private final ContextAwarePolicyEnforcement policyEnforcement;

    public ReverseGenotypeConfirmationProcessor(
        ColonyStateSetter colonyStateSetter, ContextAwarePolicyEnforcement policyEnforcement)
    {
        super(colonyStateSetter);
        this.policyEnforcement = policyEnforcement;
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean canExecuteTransition = canExecuteTransition((Colony)data);
        transitionEvaluation.setExecutable(canExecuteTransition);
        if (!canExecuteTransition)
        {
            transitionEvaluation.setNote("This can only be executed by CDA or DCC");
        }
        return transitionEvaluation;
    }

    public boolean canExecuteTransition(Colony colony)
    {
        return policyEnforcement.hasPermission(colony, "REVERSE_GENOTYPE_CONFIRMATION");
    }
}
