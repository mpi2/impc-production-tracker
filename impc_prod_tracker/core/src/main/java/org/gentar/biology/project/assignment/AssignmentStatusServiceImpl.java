package org.gentar.biology.project.assignment;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public List<AssignmentStatus> getAllAssignmentStatuses()
    {
        return StreamSupport
                .stream(assignmentStatusRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
