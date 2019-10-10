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
    public List<SearchResult> findProjects(String searchTerm)
    {
        List<SearchResult> searchResults = findProjectsByGene(searchTerm);
        if (noProjectsFoundInInternalDatabase(searchResults) && !isAccessionId(searchTerm))
        {
            searchResults = searchGeneInExternalService(searchTerm);
        }
        return searchResults;
    }

    private List<SearchResult> findProjectsByGene(String searchTerm)
    {
        List<SearchResult> searchResults = new ArrayList<>();
        ProjectFilter projectFilter =
            ProjectFilterBuilder.getInstance().withGenes(Arrays.asList(searchTerm)).build();
        List<Project> projects = projectService.getProjects(projectFilter);
        projects.forEach(p -> searchResults.add(new SearchResult(searchTerm, p, null)));
        return searchResults;
    }

    private boolean noProjectsFoundInInternalDatabase(List<SearchResult> searchResults)
    {
        return searchResults == null || searchResults.isEmpty();
    }

    private boolean isAccessionId(String searchTerm)
    {
        return searchTerm.contains(":");
    }

    private List<SearchResult> searchGeneInExternalService(String searchTerm)
    {
        List<SearchResult> searchResults = new ArrayList<>();
        if (!isSymbol(searchTerm))
        {
            searchResults = findProjectsBySynonym(searchTerm);
        }
        return searchResults;
    }

    // Checks if the search term is a "current symbol", meaning it exists in the mouse_gene
    // external table and therefore it is not a synonym.
    private boolean isSymbol(String searchTerm)
    {
        return geneExternalService.getFromExternalGenes(searchTerm) != null;
    }

    private List<SearchResult> findProjectsBySynonym(String searchTerm)
    {
        List<Project> projects;
        List<SearchResult> searchResults = new ArrayList<>();
        Gene synonym = geneExternalService.getSynonymFromExternalGenes(searchTerm);
        if (synonym != null)
        {
            searchResults = findProjectsByGene(synonym.getAccId());
            searchResults.forEach(s ->
                {
                    s.setInput(searchTerm);
                    s.setComment("Synonym of " + synonym.getSymbol());
                });
        }
        return searchResults;
    }
}
