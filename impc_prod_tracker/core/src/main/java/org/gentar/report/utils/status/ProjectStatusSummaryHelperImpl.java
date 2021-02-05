package org.gentar.report.utils.status;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProjectStatusSummaryHelperImpl implements ProjectStatusSummaryHelper {
    private final PlanStatusSummaryHelperImpl planStatusSummaryHelper;

    public ProjectStatusSummaryHelperImpl(PlanStatusSummaryHelperImpl planStatusSummaryHelper) {
        this.planStatusSummaryHelper = planStatusSummaryHelper;
    }

    public String summarisePlanStatusesForProject(String projectTpn,
                                                  Map<String, List<String>> plansForProjects,
                                                  Map<String, String> statusForPlans) {
        String summaryStatusForProject = "";
        List<String> plans = plansForProjects.get(projectTpn);
        List<String> planStatuses = plans.stream().map(statusForPlans::get).collect(Collectors.toList());
        if (planStatuses.size() == 1){
            summaryStatusForProject = planStatuses.get(0);
        }
        else if (plans.size() > 1){
            summaryStatusForProject = planStatusSummaryHelper.summariseListOfPlanStatuses(planStatuses);
        }
        return summaryStatusForProject;
    }
}
