package uk.ac.ebi.impc_prod_tracker.web.controller;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.organization.funder.Funder;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroup;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroupRepository;

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
