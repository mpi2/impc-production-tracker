package uk.ac.ebi.impc_prod_tracker.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.impc_prod_tracker.data.project.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.service.PlanService;

@RestController
@RequestMapping("/api")
public class PlanController {

    private PlanService planService;

    public PlanController(PlanService planService){
        this.planService = planService;
    }

    @GetMapping(value = {"/plans"})
    public Iterable<Plan> getPlans()
    {
        return planService.getPlans();
    }

    @PostMapping(value = {"/plans"}, consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Plan postPlan(@RequestBody Plan plan){
        return planService.createPlan(plan);
    }
}
