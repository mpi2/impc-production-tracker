package org.gentar.biology.project.search.filter;

import org.gentar.biology.allele.AlleleType;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.intention.project_intention.ProjectIntention;
import org.gentar.biology.project.search.SearchResult;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_unit.WorkUnit;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectFilterApplier
{
    public List<SearchResult> applyFilters(List<SearchResult> results, ProjectFilter filters)
    {
        List<SearchResult> filteredResults = new ArrayList<>(results);
        if (isValidList(filters.getTpns()))
        {
            filteredResults = filterWithTpn(results, filters.getTpns());
        }
        if (isValidList(filters.getWorkUnitNames()))
        {
            filteredResults = filterWithWorkUnitName(results, filters.getWorkUnitNames());
        }
        if (isValidList(filters.getWorGroupNames()))
        {
            filteredResults = filterWithWorkGroupName(results, filters.getWorGroupNames());
        }
        if (isValidList(filters.getExternalReferences()))
        {
            filteredResults = filterWithExternalReference(results, filters.getExternalReferences());
        }
        if (isValidList(filters.getIntentions()))
        {
            filteredResults = filterWithIntentions(results, filters.getIntentions());
        }
        if (isValidList(filters.getConsortiaNames()))
        {
            filteredResults = filterWithConsortia(results, filters.getConsortiaNames());
        }
        if (isValidList(filters.getPrivaciesNames()))
        {
            filteredResults = filterWithPrivacy(results, filters.getPrivaciesNames());
        }
        return filteredResults;
    }

    private List<SearchResult> filterWithTpn(List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(x -> values.contains(x.getProject().getTpn()))
            .collect(Collectors.toList());
    }

    private List<SearchResult> filterWithWorkUnitName(List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(result ->
            {
                Project project = result.getProject();
                WorkUnit match = null;
                if (project != null)
                {
                    List<WorkUnit> workUnitsByProject = project.getRelatedWorkUnits();
                    match = workUnitsByProject.stream()
                        .filter(wu -> values.contains(wu.getName()))
                        .findAny().orElse(null);
                }
                return match != null;
            })
            .collect(Collectors.toList());
    }

    private List<SearchResult> filterWithWorkGroupName(
        List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(result -> {
                Project project = result.getProject();
                WorkGroup match = null;
                if (project != null)
                {
                    List<WorkGroup> workGroupsByProject = project.getRelatedWorkGroups();
                    match = workGroupsByProject.stream()
                        .filter(wg -> values.contains(wg.getName()))
                        .findAny().orElse(null);
                }
                return match != null;
            })
            .collect(Collectors.toList());
    }

    private List<SearchResult> filterWithExternalReference(
        List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(x -> values.contains(x.getProject().getProjectExternalRef()))
            .collect(Collectors.toList());
    }

    private List<SearchResult> filterWithIntentions(
        List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(result -> {
                Project project = result.getProject();
                List<AlleleType> alleleTypes = getProjectAlleleTypes(project);
                return alleleTypes.stream()
                    .filter(x -> values.contains(x.getName()))
                    .findFirst().orElse(null) != null;
            })
            .collect(Collectors.toList());
    }

    private List<AlleleType> getProjectAlleleTypes(Project project)
    {
        List<AlleleType> alleleTypes = new ArrayList<>();
        if (project != null)
        {
            List<ProjectIntention> projectIntentions = project.getProjectIntentions();
            if (projectIntentions != null)
            {
                projectIntentions.forEach(i -> alleleTypes.add(i.getAlleleType()));
            }
        }
        return alleleTypes;
    }

    private List<SearchResult> filterWithConsortia(
        List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(x -> {
                Project project = x.getProject();
                boolean anyMatch = false;
                if (project != null)
                {
                    var relatedConsortia = project.getRelatedConsortia();
                    anyMatch = relatedConsortia.stream()
                        .filter(c -> values.contains(c.getName()))
                        .findFirst().orElse(null) != null;
                }
                return anyMatch;
            })
            .collect(Collectors.toList());
    }

    private List<SearchResult> filterWithPrivacy(
        List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(x -> values.contains(x.getProject().getPrivacy().getName()))
            .collect(Collectors.toList());
    }

    private boolean isValidList(List<String> values)
    {
        return values != null && !values.isEmpty();
    }
}
