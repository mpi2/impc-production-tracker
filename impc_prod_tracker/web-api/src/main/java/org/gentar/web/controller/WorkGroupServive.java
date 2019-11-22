package org.gentar.web.controller;

import org.springframework.stereotype.Component;
import org.gentar.organization.funder.Funder;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_group.WorkGroupRepository;

import java.util.Set;

@Component
public class WorkGroupServive {
    private WorkGroupRepository workGroupRepository;

    public WorkGroupServive(WorkGroupRepository workGroupRepository) { this.workGroupRepository = workGroupRepository; }

    public Set<Funder> getFundersByWorkGroupName(String name)
    {
        WorkGroup workGroup = workGroupRepository.findWorkGroupByName(name);
        if (workGroup != null)
        {
            return workGroup.getFunders();
        }
        return null;
    }
}
