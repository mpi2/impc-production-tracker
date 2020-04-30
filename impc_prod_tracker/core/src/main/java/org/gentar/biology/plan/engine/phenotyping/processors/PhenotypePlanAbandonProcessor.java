package org.gentar.biology.plan.engine.phenotyping.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.PlanQueryHelper;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.status.Status;
import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.statemachine.AbstractProcessor;
import org.gentar.statemachine.ProcessData;

import java.util.List;

public class PhenotypePlanAbandonProcessor extends AbstractProcessor {

    public PhenotypePlanAbandonProcessor(PlanStateSetter planStateSetter) {
        super(planStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        return canAbandonPlan((Plan) entity);
    }

    private boolean canAbandonPlan(Plan plan)
    {
        if (PhenotypingStagesDoNotExist(plan))
        {
            return true;
        }
        throw new UserOperationFailedException(
                "Plan cannot be abandoned",
                "The plan already has phenotyping stages. Please abort the plan.");
    }

    private boolean PhenotypingStagesDoNotExist(Plan plan){

        boolean result = true;

        PhenotypingAttempt phenotypingAttempt = plan.getPhenotypingAttempt();

        if (phenotypingAttempt != null) {
            result = phenotypingAttempt.getPhenotypingStages() == null;
        }
        return result;


    }

}
