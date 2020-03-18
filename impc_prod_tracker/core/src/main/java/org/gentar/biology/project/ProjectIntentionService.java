package org.gentar.biology.project;

import org.gentar.biology.project.search.filter.ProjectFilter;
import org.gentar.biology.project.search.filter.ProjectFilterBuilder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Logic related with the intentions of projects and to find projects with same intention
 */
public class ProjectIntentionService
{
    private ProjectQueryHelper projectQueryHelper;
    private ProjectService projectService;

    public ProjectIntentionService(ProjectQueryHelper projectQueryHelper, ProjectService projectService)
    {
        this.projectQueryHelper = projectQueryHelper;
        this.projectService = projectService;
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
        ProjectFilter projectFilter = ProjectFilterBuilder.getInstance().withGenes(accIds).build();
        projectsWithSameGeneIntention = projectService.getProjects(projectFilter);
        // Exclude the current project used in the comparison
        if (project.getId() != null)
        {
            projectsWithSameGeneIntention = projectsWithSameGeneIntention.stream()
                .filter(x -> !x.getId().equals(project.getId())).collect(Collectors.toList());
        }
        return projectsWithSameGeneIntention;
    }
}
