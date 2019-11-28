package org.gentar.organization.funder;

import org.gentar.organization.WorkGroupServive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.gentar.organization.funder.Funder;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class FunderController {

    private WorkGroupServive workGroupServive;

    public FunderController (WorkGroupServive workGroupServive) { this.workGroupServive = workGroupServive; }

    @GetMapping(value = {"/funders"})
    public Set<Funder> getFunders(@RequestParam String workGroupName)
    {
        return workGroupServive.getFundersByWorkGroupName(workGroupName);
    }
}
