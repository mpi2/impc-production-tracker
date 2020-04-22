package org.gentar.biology.plan;

import org.gentar.biology.colony.Colony;
import org.gentar.biology.outcome.Outcome;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlanQueryHelper
{
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
}
