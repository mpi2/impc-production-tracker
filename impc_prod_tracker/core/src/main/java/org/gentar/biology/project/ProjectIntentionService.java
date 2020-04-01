package org.gentar.biology.project;

import org.gentar.biology.project.specs.ProjectSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Logic related with the intentions of projects and to find projects with same intention
 */
@Component
public class ProjectIntentionService
{
    private ProjectQueryHelper projectQueryHelper;
    private ProjectRepository projectRepository;

    public ProjectIntentionService(
        ProjectQueryHelper projectQueryHelper, ProjectRepository projectRepository)
    {
        this.projectQueryHelper = projectQueryHelper;
        this.projectRepository = projectRepository;
    }

    /**
     * Get a list of projects who have the same intention as the project parameter.
     * The criteria to say a intention is the same is that the gene(s) in the intention is the same.
     * @param project The project to evaluate.
     * @return A list of projects that share the intention(s) in project. An empty list means there
     * are not projects with the same gene intention.
     */
    public List<Project> getProjectsWithSameGeneIntention(Project project)
    {
        List<Project> projectsWithSameGeneIntention;
        List<String> accIds = projectQueryHelper.getAccIdsByProject(project);
        projectsWithSameGeneIntention = getProjectsWithGenes(accIds);
        // Exclude the current project used in the comparison
        if (project.getId() != null)
        {
            projectsWithSameGeneIntention = projectsWithSameGeneIntention.stream()
                .filter(x -> !x.getId().equals(project.getId())).collect(Collectors.toList());
        }
        return projectsWithSameGeneIntention;
    }

    List<Project> getProjectsWithGenes(List<String> accIds)
    {
        Specification<Project> specifications = Specification.where(ProjectSpecs.withGenes(accIds));
        List<Project> projects = projectRepository.findAll(specifications);
        return projects;
    }
}
