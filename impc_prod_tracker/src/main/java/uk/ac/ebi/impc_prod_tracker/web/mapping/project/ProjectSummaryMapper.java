package uk.ac.ebi.impc_prod_tracker.web.mapping.project;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectSummaryDTO;

@Component
public class ProjectSummaryMapper
{
    public ProjectSummaryDTO toDto (ProjectDTO projectDTO)
    {
        ProjectSummaryDTO projectSummaryDTO = new ProjectSummaryDTO();
        projectSummaryDTO.setTpn(projectDTO.getTpn());
        projectDTO.setProjectGeneDTOS(projectDTO.getProjectGeneDTOS());
        projectDTO.setProjectLocationDTOS(projectDTO.getProjectLocationDTOS());
        return projectSummaryDTO;
    }
}
