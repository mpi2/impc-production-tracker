package org.gentar.biology.project.assignment;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class AssignmentStatusServiceImpl implements AssignmentStatusService
{
    private AssignmentStatusRepository assignmentStatusRepository;

    public AssignmentStatusServiceImpl(AssignmentStatusRepository assignmentStatusRepository)
    {
        this.assignmentStatusRepository = assignmentStatusRepository;
    }

    @Cacheable("assignmentStatuses")
    public AssignmentStatus getAssignmentStatusByName(String name)
    {
        return assignmentStatusRepository.findFirstByNameIgnoreCase(name);
    }
}
