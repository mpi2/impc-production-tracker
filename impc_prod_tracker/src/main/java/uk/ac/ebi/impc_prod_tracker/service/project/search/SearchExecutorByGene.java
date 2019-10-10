package uk.ac.ebi.impc_prod_tracker.service.project.search;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.gene.Gene;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.gene.GeneExternalService;
import uk.ac.ebi.impc_prod_tracker.service.project.ProjectService;
import uk.ac.ebi.impc_prod_tracker.web.controller.project.helper.ProjectFilter;
import uk.ac.ebi.impc_prod_tracker.web.controller.project.helper.ProjectFilterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
class SearchExecutorByGene implements SearchExecutor
{
    private ProjectService projectService;
    private GeneExternalService geneExternalService;

    SearchExecutorByGene(ProjectService projectService, GeneExternalService geneExternalService)
    {
        this.projectService = projectService;
        this.geneExternalService = geneExternalService;
    }

    @Override
    public List<Project> findProjects(String searchTerm)
    {
        List<Project> projects = findProjectsByGene(searchTerm);
        if (noProjectsFoundInInternalDatabase(projects) && !isAccessionId(searchTerm))
        {
            projects = searchGeneInExternalService(searchTerm);
        }
        return projects;
    }

    private List<Project> findProjectsByGene(String searchTerm)
    {
        ProjectFilter projectFilter =
            ProjectFilterBuilder.getInstance().withGenes(Arrays.asList(searchTerm)).build();
        List<Project> projects = projectService.getProjects(projectFilter);
        return projects;
    }

    private boolean noProjectsFoundInInternalDatabase(List<Project> projects)
    {
        return projects == null || projects.isEmpty();
    }

    private boolean isAccessionId(String searchTerm)
    {
        return searchTerm.contains(":");
    }

    private List<Project> searchGeneInExternalService(String searchTerm)
    {
        List<Project> projects = new ArrayList<>();
        if (!isSymbol(searchTerm))
        {
            projects = findProjectsBySynonym(searchTerm);
        }
        return projects;
    }

    // Checks if the search term is a "current symbol", meaning it exists in the mouse_gene
    // external table and therefore it is not a synonym.
    private boolean isSymbol(String searchTerm)
    {
        return geneExternalService.getFromExternalGenes(searchTerm) != null;
    }

    private List<Project> findProjectsBySynonym(String searchTerm)
    {
        List<Project> projects = new ArrayList<>();
        Gene synonym = geneExternalService.getSynonymFromExternalGenes(searchTerm);
        if (synonym != null)
        {
            projects = findProjectsByGene(synonym.getAccId());
        }
        return projects;
    }
}
