package uk.ac.ebi.impc_prod_tracker.web.mapping.plan;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.PlanDetailsDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.project.ProjectMapper;

@Component
public class PlanMapper
{
    private PlanDTOBuilder planDTOBuilder;
    private PlanDTOLinkManager planDTOLinkManager;
    private ProjectMapper projectMapper;

    public PlanMapper(PlanDTOBuilder planDTOBuilder, PlanDTOLinkManager planDTOLinkManager, ProjectMapper projectMapper)
    {
        this.planDTOBuilder = planDTOBuilder;
        this.planDTOLinkManager = planDTOLinkManager;
        this.projectMapper = projectMapper;
    }

//    public PlanDTO planToDTO(Plan plan)
//    {
//        PlanDTO planDTO = planDTOBuilder.buildPlanDTOFromPlan(plan);
//        planDTOLinkManager.addLinks(planDTO);
//        return planDTO;
//    }

//    public ProjectPlanDTO planToProjectPlanDTO(Plan plan, Project project)
//    {
//        ProjectPlanDTO projectPlanDTO = new ProjectPlanDTO();
//        PlanDTO planDTO = planToDTO(plan);
//        ProjectDetailsDTO projectDetailsDTO = projectMapper.projectToProjectDetailsDTO(project);
//        projectPlanDTO.setProjectDetailsDTO(projectDetailsDTO);
//        projectPlanDTO.setPlanDTO(planDTO);
//
//        return projectPlanDTO;
//    }

    public PlanDetailsDTO planToPlanDetailsDTO(Plan plan)
    {
        return planDTOBuilder.buildPlanDetailsDTOFromPlan(plan);
    }

//    public List<PlanDetailsDTO> plansToPlanDetailsDTOs(List<Plan> plans)
//    {
//        List<PlanDetailsDTO> planDetailsDTOs = new ArrayList<>();
//        plans.forEach(x -> planDetailsDTOs.add(planToPlanDetailsDTO(x)));
//        return planDetailsDTOs;
//    }
}
