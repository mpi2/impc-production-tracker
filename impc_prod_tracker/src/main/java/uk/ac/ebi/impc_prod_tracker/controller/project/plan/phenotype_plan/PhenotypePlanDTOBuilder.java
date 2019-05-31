package uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan.phenotyping_production.PhenotypingProductionDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan.phenotyping_production.PhenotypingProductionDTOBuilder;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.service.plan.PlanService;

@Component
public class PhenotypePlanDTOBuilder
{
    private PlanService planService;
    private PhenotypingProductionDTOBuilder phenotypingProductionDTOBuilder;

    public PhenotypePlanDTOBuilder(
        PlanService planService, PhenotypingProductionDTOBuilder phenotypingProductionDTOBuilder)
    {
        this.planService = planService;
        this.phenotypingProductionDTOBuilder = phenotypingProductionDTOBuilder;
    }

    public PhenotypePlanDTO buildPhenotypePlanDTOFromPlan(final Plan plan)
    {
        PhenotypePlanDTO phenotypePlanDTO = new PhenotypePlanDTO();
        Plan relatedProductionPlan = planService.getProductionPlanRefByPhenotypePlan(plan);
        if (relatedProductionPlan != null)
        {
            phenotypePlanDTO.setProductionPlanReference(relatedProductionPlan.getPin());
        }
        PhenotypingProductionDTO phenotypingProductionDTO =
            phenotypingProductionDTOBuilder.buildFromPlan(plan);
        phenotypePlanDTO.setPhenotypingProduction(phenotypingProductionDTO);

        return phenotypePlanDTO;
    }
}
