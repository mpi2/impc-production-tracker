package uk.ac.ebi.impc_prod_tracker.controller.project;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.ProductionPlanSummaryDTO;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project.Project;

import java.util.Objects;

@Component
public class ProjectMapper
{
    ModelMapper modelMapper;

    public ProjectMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }

    public ProjectDTO convertToDto(Project project)
    {
        Objects.requireNonNull(project, "The project is null");
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        ProjectDetailsDTO projectDetailsDTO = new ProjectDetailsDTO();
        projectDetailsDTO.setAssigmentStatusName(project.getAssignmentStatus().getName());
        projectDetailsDTO.setTpn(project.getTpn());
        return projectDTO;
    }

    public ProjectDetailsDTO convertToProjectDetailsDTO(Project project)
    {
        ProjectDetailsDTO projectDetailsDTO
            = modelMapper.map(project, ProjectDetailsDTO.class);
        return projectDetailsDTO;
    }
}
