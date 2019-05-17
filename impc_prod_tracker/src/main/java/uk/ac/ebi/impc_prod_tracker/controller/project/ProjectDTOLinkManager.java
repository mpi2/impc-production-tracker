package uk.ac.ebi.impc_prod_tracker.controller.project;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.PlanDTOLinkManager;
import java.util.List;

@Component
public class ProjectDTOLinkManager
{
    private PlanDTOLinkManager planDTOLinkManager;
    public ProjectDTOLinkManager(PlanDTOLinkManager planDTOLinkManager)
    {
        this.planDTOLinkManager = planDTOLinkManager;
    }

    public ProjectDTO addLinks(ProjectDTO projectDTO)
    {
        List<PlanDTO> planDTOList = projectDTO.getPlans();
        for (PlanDTO planDTO : planDTOList)
        {
            planDTOLinkManager.addLinks(planDTO);
        }

        return projectDTO;
    }
}
