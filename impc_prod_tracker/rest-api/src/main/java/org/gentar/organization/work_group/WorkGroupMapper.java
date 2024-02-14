package org.gentar.organization.work_group;

import org.gentar.Mapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Service;

@Service
public class WorkGroupMapper implements Mapper<WorkGroup, String>
{
    private final WorkGroupService workGroupService;

    private static final String WORK_GROUP_NOT_FOUND_ERROR = "Work group name '%s' does not exist.";

    public WorkGroupMapper(WorkGroupService workGroupService)
    {
        this.workGroupService = workGroupService;
    }

    @Override
    public String toDto(WorkGroup entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getName();
        }
        return name;
    }

    @Override
    public WorkGroup toEntity(String name)
    {
        WorkGroup workGroup = workGroupService.getWorkGroupByName(name);
        if (workGroup == null)
        {
            throw new UserOperationFailedException(WORK_GROUP_NOT_FOUND_ERROR);
        }
        return workGroup;
    }
}
