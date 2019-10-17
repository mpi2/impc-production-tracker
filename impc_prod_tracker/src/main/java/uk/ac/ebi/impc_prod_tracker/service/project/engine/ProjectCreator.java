package uk.ac.ebi.impc_prod_tracker.service.project.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;

@Component
public class ProjectCreator
{
    public Project createProject(Project project)
    {
        assignTpn(project);
        saveProject(project);
        registerCreationInHistory();
        return null;
    }

    private void assignTpn(Project project)
    {

    }

    private void saveProject(Project project)
    {

    }

    private void registerCreationInHistory()
    {

    }
}
