package uk.ac.ebi.impc_prod_tracker.service;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroup;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnitRepository;

import java.util.List;
import java.util.Set;

@Component
public class WorkUnitService {

    private WorkUnitRepository workUnitRepository;

    public WorkUnitService(WorkUnitRepository workUnitRepository){
        this.workUnitRepository = workUnitRepository;
    }

    public Set<WorkGroup> getWorkGroupsByWorkUnitName(String name)
    {
        WorkUnit workUnit = workUnitRepository.findWorkUnitByName(name);
        if (workUnit != null)
        {
            return workUnit.getWorkGroups();

        }
        return null;
    }

}
