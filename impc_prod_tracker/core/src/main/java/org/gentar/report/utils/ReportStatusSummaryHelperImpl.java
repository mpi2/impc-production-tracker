package org.gentar.report.utils;

import org.gentar.biology.project.assignment.AssignmentStatus;
import org.gentar.biology.project.assignment.AssignmentStatusServiceImpl;
import org.gentar.biology.status.Status;
import org.gentar.biology.status.StatusServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportStatusSummaryHelper {
    private final AssignmentStatusServiceImpl assignmentStatusService;
    private final StatusServiceImpl statusService;

    public Map<String, Integer> planStatusToOrdering;
    public List<String> abortedPlanStatuses;

    public ReportStatusSummaryHelper(AssignmentStatusServiceImpl assignmentStatusService,
                                     StatusServiceImpl statusService) {
        this.assignmentStatusService = assignmentStatusService;
        this.statusService = statusService;
    }

    List<Status> statuses = statusService.getAllStatuses();
    planStatusToOrdering = getOrderingByPlanStatus(statuses);
    abortedPlanStatuses = getAbortedPlanStatuses(statuses);

    private List<String> getAbortedPlanStatuses( List<Status> statuses ) {
        return statuses
                .stream()
                .filter(Status::getIsAbortionStatus)
                .map(Status::getName)
                .collect(Collectors.toList());
    }
}
