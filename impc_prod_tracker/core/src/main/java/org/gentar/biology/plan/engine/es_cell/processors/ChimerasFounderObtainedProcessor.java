package org.gentar.biology.plan.engine.es_cell.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.esCell.EsCellAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class ChimerasFounderObtainedProcessor extends AbstractProcessor
{
    public ChimerasFounderObtainedProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean mutantsExist = chimerasExist((Plan) data);
        transitionEvaluation.setExecutable(mutantsExist);
        if (!mutantsExist)
        {
            transitionEvaluation.setNote("There is not chimeras information.");
        }
        return transitionEvaluation;
    }

    private boolean chimerasExist(Plan plan)
    {
        boolean result = false;
        EsCellAttempt esCellAttempt = plan.getEsCellAttempt();
        int getTotalMaleChimeras = esCellAttempt.getTotalMaleChimeras() == null ? 0 : esCellAttempt.getTotalMaleChimeras();
        result = getTotalMaleChimeras > 0;
        return result;
    }
}
