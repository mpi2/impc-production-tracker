package org.gentar.organization.work_group;

import org.gentar.Mapper;
import org.springframework.stereotype.Service;

@Service
public class WorkGroupMapper implements Mapper<WorkGroup, String>
{
    private WorkGroupService workGroupService;

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
            workGroup = new WorkGroup();
            workGroup.setName(name);
        }

        return workGroup;
    }
}
