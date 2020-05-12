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
    private StateTransitionsManager stateTransitionManager;
    private SystemEventsExecutor systemEventsExecutor;
    private PlanStateSetter planStateSetter;
    private PlanSummaryStatusUpdater planSummaryStatusUpdater;
    private PlanStateMachineResolver planStateMachineResolver;
    private PhenotypingStageStateSetter phenotypingStageStateSetter;

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
        phenotypingStages.forEach(x -> phenotypingStageStateSetter.setInitialStatus(x));
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
        updateChildrenIfNeeded(plan);
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

    /**
     * Apply the needed changes to the children of the plan. This is in case where a state machine
     * needs to be executed in the children (because a user or system request to change status).
     * For now the only children of plan that rely in the 'update' method of a plan
     * (don't have a dedicated endpoint) to be triggered are the phenotyping stages.
     * @param plan Plan being updated.
     */
    private void updateChildrenIfNeeded(Plan plan)
    {
        List<PhenotypingStage> phenotypingStages = getPhenotypingStages(plan);
        phenotypingStages.forEach(x -> stateTransitionManager.processEvent(x));
    }

    private List<PhenotypingStage> getPhenotypingStages(Plan plan)
    {
        List<PhenotypingStage> phenotypingStages = new ArrayList<>();
        PhenotypingAttempt phenotypingAttempt = plan.getPhenotypingAttempt();
        if (phenotypingAttempt != null)
        {
            phenotypingStages.addAll(phenotypingAttempt.getPhenotypingStages());
        }
        return phenotypingStages;
    }
}
