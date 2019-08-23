package uk.ac.ebi.impc_prod_tracker.web.mapping.plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.ProjectMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlanMapper
{
    private PlanDTOBuilder planDTOBuilder;
    private PlanDTOLinkManager planDTOLinkManager;

    public PlanMapper(PlanDTOBuilder planDTOBuilder, PlanDTOLinkManager planDTOLinkManager, ProjectMapper projectMapper)
    {
        this.planDTOBuilder = planDTOBuilder;
        this.planDTOLinkManager = planDTOLinkManager;
    }

    public PlanDTO toDto(Plan plan)
    {
        return planDTOBuilder.buildPlanDTOFromPlan(plan);
    }

    public List<PlanDTO> toDtos(List<Plan> plans)
    {
        List<PlanDTO> planDTOS = new ArrayList<>();
        if (plans != null)
        {
            plans.forEach(plan -> planDTOS.add(toDto(plan)));
        }
        return planDTOS;
    }

    public PlanDTO planToPlanDTO(Plan plan)
    {
        return planDTOBuilder.buildPlanDTOFromPlan(plan);
    }

    public List<PlanDTO> plansToPlanDetailsDTOs(List<Plan> plans)
    {
        List<PlanDTO> planDTOs = new ArrayList<>();
        plans.forEach(x -> planDTOs.add(planToPlanDTO(x)));
        return planDTOs;
    }
}
