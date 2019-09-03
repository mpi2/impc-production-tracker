package uk.ac.ebi.impc_prod_tracker.web.mapping.plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.common.Constants;
import uk.ac.ebi.impc_prod_tracker.web.mapping.plan.production_plan.ProductionPlanDTOLinkManager;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;

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
            if (planDTO.getProductionPlanDTO() != null)
            {
                productionPlanDTOLinkManager.addLinks(planDTO.getProductionPlanDTO());
            }
        }

        return planDTO;
    }
}
