package org.gentar.biology.plan;

import org.gentar.biology.plan.engine.PlanStateMachineResolver;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.plan.status.PlanSummaryStatusUpdater;
import org.gentar.biology.status.StatusNames;
import org.gentar.statemachine.StateTransitionsManager;
import org.gentar.statemachine.SystemEventsExecutor;
import org.springframework.stereotype.Component;

/**
 * Class in charge of calculating and modifying the status in a plan.
 */
@Component
public class PlanStatusManager
{
    private StateTransitionsManager stateTransitionManager;
    private SystemEventsExecutor systemEventsExecutor;
    private PlanStateSetter planStateSetter;
    private PlanSummaryStatusUpdater planSummaryStatusUpdater;
    private PlanStateMachineResolver planStateMachineResolver;

    public PlanStatusManager(
        StateTransitionsManager stateTransitionManager,
        SystemEventsExecutor systemEventsExecutor,
        PlanStateSetter planStateSetter,
        PlanSummaryStatusUpdater planSummaryStatusUpdater,
        PlanStateMachineResolver planStateMachineResolver)
    {
        this.stateTransitionManager = stateTransitionManager;
        this.systemEventsExecutor = systemEventsExecutor;
        this.planStateSetter = planStateSetter;
        this.planSummaryStatusUpdater = planSummaryStatusUpdater;
        this.planStateMachineResolver = planStateMachineResolver;
    }

    public void setInitialStatus(Plan plan)
    {
        planStateSetter.setStatusByName(plan, StatusNames.PLAN_CREATED);
    }

    public void setSummaryStatus(Plan plan)
    {
        planSummaryStatusUpdater.setSummaryStatus(plan);
    }

    /**
     * Check if the changes in the plan require a change on the status.
     * @param plan Plan being updated.
     */
    public void updateStatusIfNeeded(Plan plan)
    {
        executeSystemTriggeredTransitions(plan);
        executeUserTriggeredTransitions(plan);
    }

    private void executeSystemTriggeredTransitions(Plan plan)
    {
        systemEventsExecutor.setStateMachineResolver(planStateMachineResolver);
        systemEventsExecutor.execute(plan);
    }

    private void executeUserTriggeredTransitions(Plan plan)
    {
        if (plan.getEvent() != null)
        {
            stateTransitionManager.processEvent(plan);
        }
    }
}
