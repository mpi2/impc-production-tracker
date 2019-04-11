package uk.ac.ebi.impc_prod_tracker.service;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.PlanRepository;

@Component
public class PlanService {

    private PlanRepository planRepository;

    public PlanService(PlanRepository planRepository){
        this.planRepository = planRepository;
    }

    public Iterable<Plan> getPlans() {
        return planRepository.findAll();
    }

    public Plan createPlan(Plan plan) {
        return planRepository.save(plan);
    }
}
