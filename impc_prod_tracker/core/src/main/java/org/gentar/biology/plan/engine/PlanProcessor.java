package org.gentar.biology.plan.engine;

import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessException;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.springframework.stereotype.Component;

@Component
public class PlanProcessor implements Processor
{
    @Override
    public ProcessData process(ProcessData data) throws ProcessException
    {
        ProcessEvent processEvent = data.getEvent();
        ProcessState targetFinalState = processEvent.getEndState();
        // Suppose for now the transition can be done
        ProcessState nextState = processEvent.getEndState();
        return data;
    }
}
