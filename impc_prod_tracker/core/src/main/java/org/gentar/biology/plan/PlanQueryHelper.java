package org.gentar.biology.plan;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.status.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlanQueryHelper
{
    /**
     * Gets all the colonies for a plan.
     * @param plan The plan to query.
     * @return List of {@link Colony} corresponding to outcomes that have a colony in the plan.
     */
    public static List<Colony> getColoniesByPlan(Plan plan)
    {
        List<Colony> colonies = new ArrayList<>();
        Set<Outcome> outcomes = plan.getOutcomes();
        if (outcomes != null)
        {
            outcomes.forEach(x -> {
                if (x.getColony() != null)
                {
                    colonies.add(x.getColony());
                }
            });
        }
        return colonies;
    }

    /**
     * Gets a list of Phenotyping Stages for a plan
     * @param plan Plan to check.
     * @return List of {@link PhenotypingStage} in the plan.
     */
    public static List<PhenotypingStage> getPhenotypingStages(Plan plan)
    {
        List<PhenotypingStage> phenotypingStages = new ArrayList<>();
        PhenotypingAttempt phenotypingAttempt = plan.getPhenotypingAttempt();
        Set<PhenotypingStage> phenotypingStagesSet =
            phenotypingAttempt == null ? null : phenotypingAttempt.getPhenotypingStages();
        if (phenotypingStagesSet != null)
        {
            phenotypingStages.addAll(phenotypingStagesSet);
        }
        return phenotypingStages;
    }

    /**
     * Gets a list of Phenotyping Stages statuses in a plan.
     * @param plan Plan to check.
     * @return List of {@link Status} corresponding to statuses in the phenotyping stages in this plan.
     */
    public static List<Status> getPhenotypingStagesStatuses(Plan plan)
    {
        List<Status> phenotypingStagesStatuses = new ArrayList<>();
        List<PhenotypingStage> phenotypingStages = getPhenotypingStages(plan);
        phenotypingStages.forEach(x -> phenotypingStagesStatuses.add(x.getStatus()));
        return phenotypingStagesStatuses;
    }
}
