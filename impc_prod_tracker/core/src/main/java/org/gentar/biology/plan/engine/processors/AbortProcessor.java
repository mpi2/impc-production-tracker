package org.gentar.biology.plan.engine.processors;

import org.gentar.biology.plan.Plan;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessException;
import org.gentar.statemachine.Processor;
import org.springframework.stereotype.Component;

@Component
public class AbortProcessor implements Processor
{
    @Override
    public ProcessData process(ProcessData data) throws ProcessException
    {
        ProcessEvent processEvent = data.getEvent();
        System.out.println(processEvent);
        System.out.println("Aborting!!!!!!!!!!!!");
        abortPlan((Plan)data);
        return data;
    }

    private void abortPlan(Plan plan)
    {
        if (canAbortPlan(plan))
        {

        }
    }

    private boolean canAbortPlan(Plan plan)
    {
        System.out.println("****Can plan " + plan.getPin() + " be aborted????");
        return true;
    }
}
