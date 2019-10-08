package uk.ac.ebi.impc_prod_tracker.service.project.search;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.common.types.FilterTypes;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
import uk.ac.ebi.impc_prod_tracker.service.GeneService;
import uk.ac.ebi.impc_prod_tracker.service.project.ProjectService;
import uk.ac.ebi.impc_prod_tracker.web.controller.project.helper.ProjectFilter;
import uk.ac.ebi.impc_prod_tracker.web.controller.project.helper.ProjectFilterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
class SearchExecutorByGene implements SearchExecutor
{
    private ProjectService projectService;
    private GeneService geneService;

    SearchExecutorByGene(ProjectService projectService, GeneService geneService)
    {
        this.projectService = projectService;
        this.geneService = geneService;
    }

    @Override
    public List<Project> findProjects(String input)
    {
        List<Project> projects = findProjectsByGene(input);
        return projects;
    }

    private List<Project> findProjectsByGene(String input)
    {
        geneService.getFromExternalGenes(input);
        Map<FilterTypes, List<String>> markerSymbolFilters = new HashMap<>();
        markerSymbolFilters.put(FilterTypes.GENE, Arrays.asList(input));
        ProjectFilter projectFilter =
            ProjectFilterBuilder.getInstance().withGenes(Arrays.asList(input)).build();
        List<Project> projects = projectService.getProjects(projectFilter);
        if (noProjectsFoundInInternalDatabase(projects) && !isAccessionId(input))
        {
            projects = searchGeneInExternalService(input);
        }
        return projects;
    }

    private boolean noProjectsFoundInInternalDatabase(List<Project> projects)
    {
        return projects == null || projects.isEmpty();
    }

    private boolean isAccessionId(String input)
    {
        return true;
    }

    private List<Project> searchGeneInExternalService(String input)
    {
        List<Project> projects = new ArrayList<>();
        return projects;
    }
}
