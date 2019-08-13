package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.phenotype_plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.phenotype_plan.PhenotypePlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.phenotype_plan.phenotyping_production.PhenotypingProductionDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.phenotype_plan.phenotype_production.PhenotypingProductionDTOBuilder;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
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
