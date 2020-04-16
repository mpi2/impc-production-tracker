package org.gentar.biology.plan.status;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.summary_status.ProjectSummaryStatusStamp;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class in charge of setting/updating the summary status in a plan, as well as recording that
 * change in the plan summary status stamp table.
 */
@Component
public class PlanSummaryStatusUpdater
{
    private StatusService statusService;

    public PlanSummaryStatusUpdater(StatusService statusService)
    {
        this.statusService = statusService;
    }

    public void setSummaryStatus(Plan plan)
    {
        List<Status> statuses = getChildrenStatus(plan);
        statuses.add(plan.getStatus());
        Status mostAdvancedStatus = statuses
            .stream()
            .max(Comparator.comparing(Status::getOrdering))
            .orElse(null);
        setSummaryStatus(plan, mostAdvancedStatus);
    }

    public List<Status> getChildrenStatus(Plan plan)
    {
        List<Status> statuses = new ArrayList<>();
        statuses.addAll(getOutcomesStatuses(plan));
        statuses.addAll(getPhenotypingStagesStatuses(plan));
        return statuses;
    }

    public List<Status> getOutcomesStatuses(Plan plan)
    {
        List<Status> outcomeStatuses = new ArrayList<>();
        Set<Outcome> outcomes = plan.getOutcomes();
        if (outcomes != null)
        {
            outcomes.forEach(x -> {
                Colony colony = x.getColony();
                if (colony != null)
                {
                    outcomeStatuses.add(colony.getStatus());
                }
            });
        }
        return outcomeStatuses;
    }

    public List<Status> getPhenotypingStagesStatuses(Plan plan)
    {
        List<Status> phenotypingStagesStatuses = new ArrayList<>();
        PhenotypingAttempt phenotypingAttempt = plan.getPhenotypingAttempt();
        Set<PhenotypingStage> phenotypingStages =
            phenotypingAttempt == null ? null : phenotypingAttempt.getPhenotypingStages();
        if (phenotypingStages != null )
        {
            phenotypingStages.forEach(x -> {
                phenotypingStagesStatuses.add(x.getStatus());
            });
        }
        return phenotypingStagesStatuses;
    }

    private void setSummaryStatus(Plan plan, Status summaryStatus)
    {
        String originalStatusName =
            plan.getSummaryStatus() == null ? "" : plan.getSummaryStatus().getName();
        if (!originalStatusName.equals(summaryStatus.getName()))
        {
            plan.setSummaryStatus(summaryStatus);
            registerSummaryStatusStamp(plan);
        }
    }

    private void registerSummaryStatusStamp(Plan plan)
    {
        Set<PlanSummaryStatusStamp> stamps = plan.getPlanSummaryStatusStamps();
        if (stamps == null)
        {
            stamps = new HashSet<>();
        }
        PlanSummaryStatusStamp planSummaryStatusStamp = new PlanSummaryStatusStamp();
        planSummaryStatusStamp.setPlan(plan);
        planSummaryStatusStamp.setStatus(plan.getSummaryStatus());
        planSummaryStatusStamp.setDate(LocalDateTime.now());
        stamps.add(planSummaryStatusStamp);
        plan.setPlanSummaryStatusStamps(stamps);
    }
}
