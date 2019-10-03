package uk.ac.ebi.impc_prod_tracker.service.project.search;

import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import java.util.ArrayList;
import java.util.List;

class SearchExecutorByGene implements SearchExecutor
{
    @Override
    public List<Project> findProjects(String input)
    {
        List<Project> projects = new ArrayList<>();
        Project project =  new Project();
        project.setTpn("tpn:1");
        projects.add(project);
        return projects;
    }
}
