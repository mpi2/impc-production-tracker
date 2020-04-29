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

public class PhenotypingInProgressProcessor  extends AbstractProcessor {

    public PhenotypingInProgressProcessor(PlanStateSetter planStateSetter) {
        super(planStateSetter);
    }

    @Override
    protected boolean canExecuteTransition(ProcessData entity)
    {
        Plan plan = (Plan)entity;
        PhenotypingAttempt phenotypingAttempt = plan.getPhenotypingAttempt();
        if (phenotypingAttempt != null) {
            return phenotypingAttempt.getPhenotypingStages() != null;
        }
        else {
            return false;
        }
    }
}
