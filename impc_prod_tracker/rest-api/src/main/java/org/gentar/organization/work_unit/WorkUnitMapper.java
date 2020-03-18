package org.gentar.organization.work_unit;

import org.gentar.Mapper;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.stereotype.Service;

@Service
public class WorkUnitMapper implements Mapper<WorkUnit, String>
{
    private WorkUnitService workUnitService;

    private static final String WORK_UNIT_NOT_FOUND_ERROR = "Work unit name '%s' does not exist.";

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
            throw new UserOperationFailedException(String.format(WORK_UNIT_NOT_FOUND_ERROR, workUnit));
        }

        return workUnit;
    }
}
