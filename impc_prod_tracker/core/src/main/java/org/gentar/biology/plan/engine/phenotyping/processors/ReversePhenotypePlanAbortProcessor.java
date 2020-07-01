package org.gentar.biology.plan.engine.phenotyping.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanQueryHelper;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.status.Status;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.TransitionEvaluation;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class with the logic to move a Phenotype Plan from the state "Phenotype Production Aborted"
 * to the state "Plan Created"
 */
@Component
public class ReversePhenotypePlanAbortProcessor extends AbstractProcessor {
    public ReversePhenotypePlanAbortProcessor(PlanStateSetter planStateSetter)
    {
        super(planStateSetter);
    }

    @Override
    public TransitionEvaluation evaluateTransition(ProcessEvent transition, ProcessData data)
    {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(transition);
        boolean areAllPhenotypingStagesAborted = areAllPhenotypingStagesAborted((Plan) data);
        transitionEvaluation.setExecutable(!areAllPhenotypingStagesAborted);
        if (areAllPhenotypingStagesAborted)
        {
            transitionEvaluation.setNote(
                    "All phenotyping stages associated with the plan are aborted. Please re-activate one first.");
        }
        return transitionEvaluation;
    }

    private boolean areAllPhenotypingStagesAborted(Plan plan)
    {
        boolean result = true;
        List<Status> phenotypingStagesStatuses = PlanQueryHelper.getPhenotypingStagesStatuses(plan);
        if (!phenotypingStagesStatuses.isEmpty())
        {
            result = phenotypingStagesStatuses.stream().allMatch(Status::getIsAbortionStatus);
        }
        return result;
    }
}
