package org.gentar.biology.plan.engine.phenotyping.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;

public class PhenotypePlanAbandonProcessor extends AbstractProcessor {

    public PhenotypePlanAbandonProcessor(PlanStateSetter planStateSetter) {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean canAbandonPlan = canAbandonPlan((Plan) data);
        transitionEvaluation.setExecutable(canAbandonPlan);
        if (!canAbandonPlan)
        {
            transitionEvaluation.setNote(
                "The plan already has phenotyping stages. Please abort the plan.");
        }
        return transitionEvaluation;
    }

    private boolean canAbandonPlan(Plan plan)
    {
        if (phenotypingStagesDoNotExist(plan))
        {
            return true;
        }
        throw new UserOperationFailedException(
                "Plan cannot be abandoned",
                "The plan already has phenotyping stages. Please abort the plan.");
    }

    private boolean phenotypingStagesDoNotExist(Plan plan){

        boolean result = true;

        PhenotypingAttempt phenotypingAttempt = plan.getPhenotypingAttempt();

        if (phenotypingAttempt != null) {
            result = phenotypingAttempt.getPhenotypingStages() == null;
        }
        return result;


    }

}
