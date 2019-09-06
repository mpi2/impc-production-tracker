package uk.ac.ebi.impc_prod_tracker.web.mapping.project;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectSummaryDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectMapper
{
    private ProjectDTOBuilder projectDTOBuilder;

    public ProjectMapper(ProjectDTOBuilder projectDTOBuilder)
    {
        this.projectDTOBuilder = projectDTOBuilder;
    }

    public ProjectDTO projectToDTO(Project project)
    {
        ProjectDTO projectDTO = projectDTOBuilder.buildProjectDTOFromProject(project);
        return projectDTO;
    }

    public List<ProjectDTO> projectsToDTOs(List<Project> project)
    {
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        project.forEach(p -> projectDTOList.add(projectToDTO(p)));
        return projectDTOList;
    }
}
