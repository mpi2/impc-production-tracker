package org.gentar.report.utils;

import org.gentar.biology.project.assignment.AssignmentStatusServiceImpl;
import org.gentar.biology.status.StatusServiceImpl;

public class PlanStatusSummaryHelperImpl {

    private final AssignmentStatusServiceImpl assignmentStatusService;
    private final StatusServiceImpl statusService;


    public PlanStatusSummaryHelperImpl(AssignmentStatusServiceImpl assignmentStatusService,
                                       StatusServiceImpl statusService) {
        this.assignmentStatusService = assignmentStatusService;
        this.statusService = statusService;
    }
}
