package org.gentar.organization.work_group;

import org.gentar.organization.work_unit.WorkUnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class WorkGroupController {

    private final WorkUnitService workUnitService;
    private final WorkGroupService workGroupService;

    public WorkGroupController(WorkUnitService workUnitService, WorkGroupService workGroupService)
    {
        this.workUnitService = workUnitService;
        this.workGroupService = workGroupService;
    }

    @GetMapping(value = {"/workGroups"})
    public Set<WorkGroup> getWorkGroup()
    {
        return workGroupService.getAll();
    }
    @GetMapping(value = {"/workGroupsByWorkUnit"})
    public Set<WorkGroup> getWorkGroup(@RequestParam String workUnitName)
    {
        return workUnitService.getWorkGroupsByWorkUnitName(workUnitName);
    }

}
