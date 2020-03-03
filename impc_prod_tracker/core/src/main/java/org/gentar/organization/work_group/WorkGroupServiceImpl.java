package org.gentar.organization.work_group;

import org.gentar.organization.funder.Funder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class WorkGroupServiceImpl implements WorkGroupService
{
    private WorkGroupRepository workGroupRepository;

    public WorkGroupServiceImpl(WorkGroupRepository workGroupRepository) { this.workGroupRepository = workGroupRepository; }

    public Set<Funder> getFundersByWorkGroupName(String name)
    {
        WorkGroup workGroup = workGroupRepository.findWorkGroupByName(name);
        if (workGroup != null)
        {
            return workGroup.getFunders();
        }
        return null;
    }

    public WorkGroup getWorkGroupByName(String name) {
        return workGroupRepository.findWorkGroupByNameIgnoreCase(name);
    }
}
