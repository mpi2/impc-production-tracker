package org.gentar.organization.work_group;

import org.gentar.organization.funder.Funder;
import org.gentar.organization.work_unit.WorkUnitRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class WorkGroupServiceImpl implements WorkGroupService
{
    private final WorkGroupRepository workGroupRepository;
    private final WorkUnitRepository workUnitRepository;

    public WorkGroupServiceImpl(
        WorkGroupRepository workGroupRepository, WorkUnitRepository workUnitRepository)
    {
        this.workGroupRepository = workGroupRepository;
        this.workUnitRepository = workUnitRepository;
    }

    @Override
    public Set<Funder> getFundersByWorkGroupName(String name)
    {
        WorkGroup workGroup = workGroupRepository.findWorkGroupByName(name);
        if (workGroup != null)
        {
            return workGroup.getFunders();
        }
        return null;
    }

    @Override
    @Cacheable("workGroupNames")
    public WorkGroup getWorkGroupByName(String name)
    {
        return workGroupRepository.findWorkGroupByNameIgnoreCase(name);
    }

    @Override
    public Set<WorkGroup> getAll()
    {
        return workGroupRepository.findAll();
    }

    @Override
    public Map<String, List<String>> getWorkGroupsNamesByWorkUnitNamesMap()
    {
        Map<String, List<String>> map = new HashMap<>();
        var workUnits = workUnitRepository.findAll();
        workUnits.forEach(workUnit -> {
            var workGroups = workUnit.getWorkGroups();
            List<String> workGroupsNames = new ArrayList<>();
            if (workGroups != null)
            {
                workGroups.forEach(workGroup -> workGroupsNames.add(workGroup.getName()));
            }
            map.put(workUnit.getName(), workGroupsNames);
        });
        return map;
    }


}
