package uk.ac.ebi.impc_prod_tracker.service.project.search;

import uk.ac.ebi.impc_prod_tracker.web.dto.project.ProjectDTO;

import java.util.ArrayList;
import java.util.List;

class SearchExecutorByGene implements SearchExecutor
{
    @Override
    public List<ProjectDTO> findProjects(String input)
    {
        List<ProjectDTO> projects = new ArrayList<>();
        ProjectDTO projectDTO =  new ProjectDTO();
        projectDTO.setTpn("tpn:1");
        projects.add(projectDTO);
        return projects;
    }
}
