package org.gentar.report.utils.status;

import org.gentar.biology.status.StatusServiceImpl;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanStatusSummaryHelperImpl implements PlanStatusSummaryHelper {

    private final StatusServiceImpl statusService;


    public PlanStatusSummaryHelperImpl(StatusServiceImpl statusService) {
        this.statusService = statusService;
    }

    public String summariseListOfPlanStatuses(List<String> planStatuses) {

        String status;

        List<String> activePlanStatuses = planStatuses
                .stream()
                .filter(i -> !(statusService.getAbortedPlanStatuses().contains(i)))
                .collect(Collectors.toList());

        if (!activePlanStatuses.isEmpty()){
            status = comparePlanStatusToCalculateSummary(activePlanStatuses);
        } else {
            status = comparePlanStatusToCalculateSummary(planStatuses);
        }

        return status;
    }

    private String comparePlanStatusToCalculateSummary(List<String> planStatuses) {
        String status;
        status = planStatuses
                .stream()
                .max(Comparator.comparing(i -> statusService.getPlanStatusOrderingMap().get(i)))
                .get();
        return status;
    }
}
