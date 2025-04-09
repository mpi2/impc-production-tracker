package org.gentar.biology.plan;

import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageStateSetter;
import org.gentar.biology.plan.engine.PlanStateMachineResolver;
import org.gentar.biology.plan.engine.PlanStateSetter;
import org.gentar.biology.plan.status.PlanSummaryStatusUpdater;
import org.gentar.biology.status.StatusNames;
import org.gentar.statemachine.StateTransitionsManager;
import org.gentar.statemachine.SystemEventsExecutor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Class in charge of calculating and modifying the status in a plan.
 */
@Component
public class PlanStatusManager
{
    private final StateTransitionsManager stateTransitionManager;
    private final SystemEventsExecutor systemEventsExecutor;
    private final PlanStateSetter planStateSetter;
    private final PlanSummaryStatusUpdater planSummaryStatusUpdater;
    private final PlanStateMachineResolver planStateMachineResolver;
    private final PhenotypingStageStateSetter phenotypingStageStateSetter;

    public PlanStatusManager(
        StateTransitionsManager stateTransitionManager,
        SystemEventsExecutor systemEventsExecutor,
        PlanStateSetter planStateSetter,
        PlanSummaryStatusUpdater planSummaryStatusUpdater,
        PlanStateMachineResolver planStateMachineResolver,
        PhenotypingStageStateSetter phenotypingStageStateSetter)
    {
        this.stateTransitionManager = stateTransitionManager;
        this.systemEventsExecutor = systemEventsExecutor;
        this.planStateSetter = planStateSetter;
        this.planSummaryStatusUpdater = planSummaryStatusUpdater;
        this.planStateMachineResolver = planStateMachineResolver;
        this.phenotypingStageStateSetter = phenotypingStageStateSetter;
    }

    public void setInitialStatus(Plan plan)
    {
        planStateSetter.setStatusByName(plan, StatusNames.PLAN_CREATED);
    }

    public void setChildrenInitialStatuses(Plan plan)
    {
        List<PhenotypingStage> phenotypingStages = getPhenotypingStages(plan);
        phenotypingStages.forEach(phenotypingStageStateSetter::setInitialStatus);
    }

    public void setSummaryStatus(Plan plan)
    {
        planSummaryStatusUpdater.setSummaryStatus(plan);
    }

    /**
     * Executes all the status related changes. The changes can be because the user explicitly
     * requested them in the state machine or because of the data that causes a system triggered
     * transition.
     * This method will:
     * - Execute the system triggered statusTransitions that are needed.
     * - Execute the user triggered transition that the user specified in the event.
     * - Update (if needed) the summary status of the plan.
     * @param plan Plan that is being evaluated.
     */
    public void updateStatusIfNeeded(Plan plan)
    {
        executeSystemTriggeredTransitions(plan);
        executeUserTriggeredTransitions(plan);
        // Once the final state for the plan is calculated, we can calculate the summary status
        setSummaryStatus(plan);
    }

    private void executeSystemTriggeredTransitions(Plan plan)
    {
        systemEventsExecutor.setStateMachineResolver(planStateMachineResolver);
        systemEventsExecutor.execute(plan);
    }

    private void executeUserTriggeredTransitions(Plan plan)
    {
        if (plan.getProcessDataEvent() != null)
        {
            stateTransitionManager.processEvent(plan);
        }
    }

    private List<PhenotypingStage> getPhenotypingStages(Plan plan)
    {
        List<PhenotypingStage> phenotypingStages = new ArrayList<>();
        PhenotypingAttempt phenotypingAttempt = plan.getPhenotypingAttempt();
        if (phenotypingAttempt != null)
        {
            if (phenotypingAttempt.getPhenotypingStages() != null)
            {
                phenotypingStages.addAll(phenotypingAttempt.getPhenotypingStages());
            }
        }
        return phenotypingStages;
    }
}
