package org.gentar.report.utils.status;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GeneStatusSummaryHelperImpl implements GeneStatusSummaryHelper{

    private final PlanStatusSummaryHelperImpl planStatusSummaryHelper;
    private final ProjectStatusSummaryHelperImpl projectStatusSummaryHelper;

    public GeneStatusSummaryHelperImpl(PlanStatusSummaryHelperImpl planStatusSummaryHelper,
                                       ProjectStatusSummaryHelperImpl projectStatusSummaryHelper)
    {
        this.planStatusSummaryHelper = planStatusSummaryHelper;
        this.projectStatusSummaryHelper = projectStatusSummaryHelper;
    }



    public Map<String, String> calculateGenePlanSummaryStatus(Map<String, List<String>> projectsForGenes,
                                                              Map<String, List<String>> plansForProjects,
                                                              Map<String, String> statusForPlans)
    {
        return  projectsForGenes
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    return summariseGenePlanStatuses(e.getValue(), plansForProjects, statusForPlans);
                }));

    }



    private String summariseGenePlanStatuses(List<String> projects,
                                            Map<String, List<String>> plansForProjects,
                                            Map<String, String> statusForPlans)
    {
        List<String> summaryPlanStatusForAllProjects = projects
                .stream()
                .map(x -> projectStatusSummaryHelper.summarisePlanStatusesForProject(x, plansForProjects, statusForPlans))
                .collect(Collectors.toList());

        String summaryPlanStatusForGene = planStatusSummaryHelper.summariseListOfPlanStatuses(summaryPlanStatusForAllProjects);

        return summaryPlanStatusForGene;

    }
}
