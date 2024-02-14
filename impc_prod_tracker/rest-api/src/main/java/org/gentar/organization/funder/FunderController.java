package org.gentar.organization.funder;

import org.gentar.organization.work_group.WorkGroupService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class FunderController {

    private final WorkGroupService workGroupService;

    public FunderController (WorkGroupService workGroupService) { this.workGroupService = workGroupService; }

    @GetMapping(value = {"/funders"})
    public Set<Funder> getFunders(@RequestParam String workGroupName)
    {
        return workGroupService.getFundersByWorkGroupName(workGroupName);
    }
}
