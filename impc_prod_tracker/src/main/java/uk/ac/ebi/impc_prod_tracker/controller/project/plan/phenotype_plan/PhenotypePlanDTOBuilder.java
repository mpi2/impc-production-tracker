package uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;

@Component
public class PhenotypePlanDTOBuilder
{
    private PlanService planService;

    public PhenotypePlanDTOBuilder(PlanService planService)
    {
        this.planService = planService;
    }

    public PhenotypePlanDTO buildPhenotypePlanDTOFromPlan(final Plan plan)
    {
        PhenotypePlanDTO phenotypePlanDTO = new PhenotypePlanDTO();
        Plan relatedProductionPlan = planService.getProductionPlanRefByPhenotypePlan(plan);
        if (relatedProductionPlan != null)
        {
            phenotypePlanDTO.setProductionPlanReference(relatedProductionPlan.getPin());
        }

        return phenotypePlanDTO;
    }
}
