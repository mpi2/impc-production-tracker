package org.gentar.biology.plan.status;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.type.OutcomeTypeName;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.specimen.Specimen;
import org.gentar.biology.status.Status;
import org.gentar.exceptions.SystemOperationFailedException;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Class in charge of setting/updating the summary status in a plan, as well as recording that
 * change in the plan summary status stamp table.
 */
@Component
public class PlanSummaryStatusUpdater
{
    public void setSummaryStatus(Plan plan)
    {
        List<Status> statuses = getChildrenStatus(plan);
        statuses.add(plan.getProcessDataStatus());
        Status mostAdvancedStatus = getMostAdvanceStatusIgnoringAborted(statuses);
        if (mostAdvancedStatus == null)
        {
            mostAdvancedStatus = plan.getProcessDataStatus();
        }
        setSummaryStatus(plan, mostAdvancedStatus);
    }

    private Status getMostAdvanceStatusIgnoringAborted(List<Status> statuses)
    {
        return statuses
            .stream()
            .filter(Objects::nonNull)
            .filter(x -> !x.getIsAbortionStatus())
            .max(Comparator.comparing(Status::getOrdering))
            .orElse(null);
    }

    private List<Status> getChildrenStatus(Plan plan)
    {
        List<Status> statuses = new ArrayList<>();
        statuses.addAll(getOutcomesStatuses(plan));
        statuses.addAll(getPhenotypingStagesStatuses(plan));
        return statuses;
    }

    private List<Status> getOutcomesStatuses(Plan plan)
    {
        List<Status> outcomeStatuses = new ArrayList<>();
        Set<Outcome> outcomes = plan.getOutcomes();
        if (outcomes != null)
        {
            outcomes.forEach(x -> {
                if (x.getOutcomeType().getName().equals(OutcomeTypeName.COLONY.getLabel()))
                {
                    Colony colony = x.getColony();
                    if (colony != null)
                    {
                        outcomeStatuses.add(colony.getProcessDataStatus());
                    }
                }

                else if (x.getOutcomeType().getName().equals(OutcomeTypeName.SPECIMEN.getLabel()))
                {
                    Specimen specimen = x.getSpecimen();
                    if (specimen != null)
                    {
                        outcomeStatuses.add(specimen.getProcessDataStatus());
                    }
                }
            });
        }
        return outcomeStatuses;
    }

    private List<Status> getPhenotypingStagesStatuses(Plan plan)
    {
        List<Status> phenotypingStagesStatuses = new ArrayList<>();
        PhenotypingAttempt phenotypingAttempt = plan.getPhenotypingAttempt();
        Set<PhenotypingStage> phenotypingStages =
            phenotypingAttempt == null ? null : phenotypingAttempt.getPhenotypingStages();
        if (phenotypingStages != null )
        {
            phenotypingStages.forEach(x -> {
                phenotypingStagesStatuses.add(x.getProcessDataStatus());
            });
        }
        return phenotypingStagesStatuses;
    }

    private void setSummaryStatus(Plan plan, Status summaryStatus)
    {
        if (summaryStatus == null)
        {
            throw new SystemOperationFailedException(
                "Summary status cannot be set",
                "The summary status for plan " + plan.toString() + " was null");
        }
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
