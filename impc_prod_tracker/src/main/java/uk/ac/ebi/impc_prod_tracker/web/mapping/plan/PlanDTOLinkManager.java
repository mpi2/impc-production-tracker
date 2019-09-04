package uk.ac.ebi.impc_prod_tracker.web.mapping.plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;

@Component
public class PlanDTOLinkManager
{
//    private PlanDTOLinkManager planDTOLinkManager;
//
//    public PlanDTOLinkManager(PlanDTOLinkManager planDTOLinkManager)
//    {
//        this.planDTOLinkManager = planDTOLinkManager;
//    }

    public PlanDTO addLinks(PlanDTO planDTO)
    {
        if (planDTO != null)
        {
//           planDTOLinkManager.addLinks(planDTO.getAttemptDTO());
        }

        return planDTO;
    }
}
