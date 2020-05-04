package org.gentar.biology.plan.engine.crispr.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

@Component
public class AttemptInProgressProcessor extends AbstractProcessor
{
    public AttemptInProgressProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean crisprAttemptExist = crisprAttemptExist(data);
        transitionEvaluation.setExecutable(crisprAttemptExist);
        if (!crisprAttemptExist)
        {
            transitionEvaluation.setNote(
                "There is not a crispr attempt yet");
        }
        return transitionEvaluation;
    }

    public boolean crisprAttemptExist(ProcessData entity)
    {
        Plan plan = (Plan)entity;
        return plan.getCrisprAttempt() != null;
    }
}
