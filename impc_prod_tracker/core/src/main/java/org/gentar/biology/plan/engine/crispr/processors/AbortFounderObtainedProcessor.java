package org.gentar.biology.plan.engine.crispr.processors;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanQueryHelper;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AbortFounderObtainedProcessor extends AbstractProcessor
{
    public AbortFounderObtainedProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean areAllColoniesAborted = areAllColoniesAborted((Plan) data);
        transitionEvaluation.setExecutable(areAllColoniesAborted);
        if (!areAllColoniesAborted)
        {
            transitionEvaluation.setNote(
                "The plan has colonies that are not aborted. Please abort them first");
        }
        return transitionEvaluation;
    }

    private boolean areAllColoniesAborted(Plan plan)
    {
        return PlanQueryHelper.areAllColoniesByPlanAborted(plan);
    }
}
