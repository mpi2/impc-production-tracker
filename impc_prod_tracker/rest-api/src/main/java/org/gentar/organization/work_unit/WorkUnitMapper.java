package org.gentar.organization.work_unit;

import org.gentar.Mapper;
import org.springframework.stereotype.Service;

@Service
public class WorkUnitMapper implements Mapper<WorkUnit, String>
{
    private WorkUnitService workUnitService;

    public WorkUnitMapper(WorkUnitService workUnitService)
    {
        this.workUnitService = workUnitService;
    }

    @Override
    public String toDto(WorkUnit entity)
    {
        String name = null;
        if (entity != null)
        {
            name = entity.getFullName();
        }
        return name;
    }

    @Override
    public WorkUnit toEntity(String name)
    {
        WorkUnit workUnit = workUnitService.getWorkUnitByName(name);
        if (workUnit == null)
        {
            workUnit = new WorkUnit();
            workUnit.setName(name);
        }

        return workUnit;
    }
}
