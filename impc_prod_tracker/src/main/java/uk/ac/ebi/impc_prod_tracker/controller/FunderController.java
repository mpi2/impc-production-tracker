package uk.ac.ebi.impc_prod_tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.data.organization.funder.Funder;

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
