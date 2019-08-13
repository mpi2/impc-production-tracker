package uk.ac.ebi.impc_prod_tracker.web.mapping.project;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectMapper
{
    private ProjectDTOBuilder projectDTOBuilder;
    private ProjectLinkManager projectLinkManager;

    public ProjectMapper(ProjectDTOBuilder projectDTOBuilder, ProjectLinkManager projectLinkManager)
    {
        this.projectDTOBuilder = projectDTOBuilder;
        this.projectLinkManager = projectLinkManager;
    }

    public ProjectDTO projectToDTO(Project project)
    {
        ProjectDTO projectDTO = projectDTOBuilder.buildProjectDTOFromProject(project);
        projectLinkManager.addLinks(projectDTO);
        return projectDTO;
    }

    public List<ProjectDTO> projectsToDTOs(List<Project> project)
    {
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        project.forEach(p -> projectDTOList.add(projectToDTO(p)));
        return projectDTOList;
    }

//    public ProjectDetailsDTO projectToProjectDetailsDTO(Project project)
//    {
//        return projectDTOBuilder.buildProjectDetailsDTOFromProject(project);
//    }
//
//    public ProjectSummaryDTO projectToProjectSummaryDTO(Project project)
//    {
//        ProjectSummaryDTO projectSummaryDTO = new ProjectSummaryDTO();
//
//        projectSummaryDTO.setProjectDetailsDTO(
//            projectDTOBuilder.buildProjectDetailsDTOFromProject(project));
//
//        return projectSummaryDTO;
//    }
}
