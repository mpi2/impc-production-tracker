package org.gentar.biology.project.assignment_status;

import org.springframework.stereotype.Component;

@Component
public class AssignmentStatusServiceImpl implements AssignmentStatusService
{
    private AssignmentStatusRepository assignmentStatusRepository;

    public AssignmentStatusServiceImpl(AssignmentStatusRepository assignmentStatusRepository)
    {
        this.assignmentStatusRepository = assignmentStatusRepository;
    }

    public AssignmentStatus getAssignmentStatusByName(String name)
    {
        return assignmentStatusRepository.findFirstByNameIgnoreCase(name);
    }
}
