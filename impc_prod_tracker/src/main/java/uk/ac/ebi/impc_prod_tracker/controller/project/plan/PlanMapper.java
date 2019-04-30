package uk.ac.ebi.impc_prod_tracker.controller.project.plan;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.phenotype_plan.PhenotypePlanSummaryDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.ProductionPlanSummaryDTO;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;

@Component
public class PlanMapper
{
    ModelMapper modelMapper;

    public PlanMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    public PlanDetailsDTO convertToPlanDetailsDto(Plan plan)
    {
        PlanDetailsDTO planDetailsDTO = modelMapper.map(plan, PlanDetailsDTO.class);
        return planDetailsDTO;
    }

    public ProductionPlanSummaryDTO convertToProductionPlanSummaryDto(Plan plan)
    {
        ProductionPlanSummaryDTO productionPlanSummaryDTO
            = modelMapper.map(plan, ProductionPlanSummaryDTO.class);
        return productionPlanSummaryDTO;
    }

    public PhenotypePlanSummaryDTO convertToPhenotypePlanSummaryDto(Plan plan)
    {
        PhenotypePlanSummaryDTO phenotypePlanSummaryDTO
            = modelMapper.map(plan, PhenotypePlanSummaryDTO.class);
        return phenotypePlanSummaryDTO;
    }
}
