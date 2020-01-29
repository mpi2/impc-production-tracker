package org.gentar.biology.plan.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gentar.biology.plan.Plan;
import org.gentar.statemachine.AbstractStateTransitionsManager;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessException;
import org.gentar.statemachine.Processor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class PlanStateTransitionManager extends AbstractStateTransitionsManager
{
    private final ApplicationContext context;

    @Override
    protected ProcessData initializeState(ProcessData sdata) throws ProcessException
    {
        Plan data = (Plan) sdata;
        log.info("Initial state: ");
        return data;
    }

    @Override
    protected ProcessData processStateTransition(ProcessData sdata) throws ProcessException
    {
        Plan data = (Plan) sdata;
        try {
            log.info("Pre-event: " + data.getEvent().toString());
            Class<? extends Processor> processor = data.getEvent().nextStepProcessor();
            data = (Plan) this.context.getBean(processor).process(data);
            log.info("Post-event: " + data.getEvent().toString());
            log.info("??*************************************");

        } catch (Exception e) {
            String error = e.getMessage();
        }
        return data;
    }
}
