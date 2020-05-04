package org.gentar.biology.plan.engine.crispr.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanQueryHelper;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.specimen.Specimen;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;


@Component
public class AbortEmbryosObtainedProcessor extends AbstractProcessor {

    // This class is designed to be used with haplo-essential crispr production plans

    public AbortEmbryosObtainedProcessor(PlanStateSetter planStateSetter) {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean areAllSpecimensAborted = areAllSpecimensAborted((Plan) data);
        transitionEvaluation.setExecutable(areAllSpecimensAborted);
        if (!areAllSpecimensAborted)
        {
            transitionEvaluation.setNote(
                "The plan has specimens that are not aborted. Please abort them first");
        }
        return transitionEvaluation;
    }

    private boolean areAllSpecimensAborted(Plan plan)
    {
        return PlanQueryHelper.areAllSpecimensByPlanAborted(plan);
    }
}
