package org.gentar.biology.plan.engine.phenotyping.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class PhenotypePlanAbandonProcessor extends AbstractProcessor
{
    public PhenotypePlanAbandonProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean phenotypingStagesDoNotExist = phenotypingStagesDoNotExist((Plan) data);
        transitionEvaluation.setExecutable(phenotypingStagesDoNotExist);
        if (!phenotypingStagesDoNotExist)
        {
            transitionEvaluation.setNote(
                "The plan already has phenotyping stages. Please abort the plan.");
        }
        return transitionEvaluation;
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
