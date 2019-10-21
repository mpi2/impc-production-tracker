package uk.ac.ebi.impc_prod_tracker.service.project.engine;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.ProjectRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
@Transactional
public class ProjectCreator
{
    private ProjectRepository projectRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public ProjectCreator(ProjectRepository projectRepository)
    {
        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project)
    {
        assignTpn(project);
        Project createdProject = saveProject(project);
        registerCreationInHistory();
        return createdProject;
    }

    private void assignTpn(Project project)
    {
        project.setTpn("test");
    }

    private Project saveProject(Project project)
    {
        entityManager.persist(project);
        project.setTpn(buildTpn(project.getId()));
        return project;
    }

    private void registerCreationInHistory()
    {

    }

    private String buildTpn(Long id)
    {
        String identifier = String.format("%0" + 9 + "d", id);
        identifier = "TPN:" + identifier;
        return identifier;
    }

}
