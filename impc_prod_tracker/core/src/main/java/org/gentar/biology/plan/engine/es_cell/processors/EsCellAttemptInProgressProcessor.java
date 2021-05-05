package org.gentar.biology.plan.engine.es_cell.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class EsCellAttemptInProgressProcessor extends AbstractProcessor
{
    public EsCellAttemptInProgressProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean esCellAttemptExist = esCellAttemptExist(data);
        transitionEvaluation.setExecutable(esCellAttemptExist);
        if (!esCellAttemptExist)
        {
            transitionEvaluation.setNote(
                    "There is not an es cell attempt yet");
        }
        return transitionEvaluation;
    }

    public boolean esCellAttemptExist(ProcessData entity)
    {
        Plan plan = (Plan)entity;
        return plan.getEsCellAttempt() != null;
    }
}
