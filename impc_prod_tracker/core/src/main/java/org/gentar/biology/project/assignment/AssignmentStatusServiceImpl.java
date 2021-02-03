package org.gentar.biology.project.assignment;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class AssignmentStatusServiceImpl implements AssignmentStatusService
{
    private AssignmentStatusRepository assignmentStatusRepository;
    private Map<String, Integer> assignmentStatusesToOrdering;

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

    public Map<String, Integer>  getAssignmentStatusOrderingMap()
    {
        if (null == assignmentStatusesToOrdering) {
            generateAssignmentStatusOrderingMap();
        }
        return assignmentStatusesToOrdering;
    }

    private void generateAssignmentStatusOrderingMap()
    {
        List<AssignmentStatus> assignmentStatuses = getAllAssignmentStatuses();
        assignmentStatusesToOrdering =
            assignmentStatuses
            .stream()
            .collect(Collectors.toMap(AssignmentStatus::getName, AssignmentStatus::getOrdering));

    }

}
