package org.gentar.web.controller;

import org.gentar.organization.work_unit.WorkUnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.gentar.organization.work_group.WorkGroup;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class WorkGroupController {

    private WorkUnitService workUnitService;

    public WorkGroupController (WorkUnitService workUnitService){
        this.workUnitService = workUnitService;
    }

    @GetMapping(value = {"/workGroups"})
    public Set<WorkGroup> getWorkGroup(@RequestParam String workUnitName)
    {
        return workUnitService.getWorkGroupsByWorkUnitName(workUnitName);
    }

}
