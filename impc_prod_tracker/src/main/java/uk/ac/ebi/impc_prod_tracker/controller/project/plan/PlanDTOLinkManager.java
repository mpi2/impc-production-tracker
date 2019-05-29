package uk.ac.ebi.impc_prod_tracker.controller.project.plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.common.Constants;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.ProductionPlanDTOLinkManager;

@Component
public class PlanDTOLinkManager
{
    private ProductionPlanDTOLinkManager productionPlanDTOLinkManager;

    public PlanDTOLinkManager(ProductionPlanDTOLinkManager productionPlanDTOLinkManager)
    {
        this.productionPlanDTOLinkManager = productionPlanDTOLinkManager;
    }

    public PlanDTO addLinks(PlanDTO planDTO)
    {
        if (Constants.PRODUCTION_TYPE.equals(planDTO.getPlanDetailsDTO().getPlanTypeName()))
        {
            productionPlanDTOLinkManager.addLinks(planDTO.getProductionPlanDTO());
        }

        return planDTO;
    }
}
