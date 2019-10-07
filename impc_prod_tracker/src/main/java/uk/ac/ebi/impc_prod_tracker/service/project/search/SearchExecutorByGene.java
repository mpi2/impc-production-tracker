package uk.ac.ebi.impc_prod_tracker.service.project.search;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.common.types.FilterTypes;
import uk.ac.ebi.impc_prod_tracker.data.biology.project.Project;
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

    SearchExecutorByGene(ProjectService projectService)
    {
        this.projectService = projectService;
    }

    @Override
    public List<Project> findProjects(String input)
    {
        List<Project> projects = findProjectsByMarkerSymbol(input);
        return projects;
    }

    private List<Project> findProjectsByMarkerSymbol(String input)
    {
        Map<FilterTypes, List<String>> markerSymbolFilters = new HashMap<>();
        markerSymbolFilters.put(FilterTypes.MARKER_SYMBOL, Arrays.asList(input));
        ProjectFilter projectFilter =
            ProjectFilterBuilder.getInstance().withMarkerSymbols(Arrays.asList(input)).build();
        return projectService.getProjects(projectFilter);

    }
}
