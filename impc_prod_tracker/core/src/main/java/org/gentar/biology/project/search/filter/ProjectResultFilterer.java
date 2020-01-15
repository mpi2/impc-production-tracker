package org.gentar.biology.project.search.filter;

import org.gentar.biology.project.Project;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectResultFilterer
{
    private ProjectPredicates projectPredicates;

    public ProjectResultFilterer(ProjectPredicates projectPredicates)
    {
        this.projectPredicates = projectPredicates;
    }

    public List<Project> applyFilters(List<Project> projects, ProjectFilter filters)
    {
        List<Project> filteredResults = new ArrayList<>(projects);
        if (isValidList(filters.getTpns()))
        {
            filteredResults = filterWithTpn(projects, filters.getTpns());
        }
        if (isValidList(filters.getWorkUnitNames()))
        {
            filteredResults = filterWithWorkUnitName(projects, filters.getWorkUnitNames());
        }
        if (isValidList(filters.getWorGroupNames()))
        {
            filteredResults = filterWithWorkGroupName(projects, filters.getWorGroupNames());
        }
        if (isValidList(filters.getExternalReferences()))
        {
            filteredResults = filterWithExternalReference(projects, filters.getExternalReferences());
        }
        if (isValidList(filters.getIntentions()))
        {
            filteredResults = filterWithIntentions(projects, filters.getIntentions());
        }
        if (isValidList(filters.getConsortiaNames()))
        {
            filteredResults = filterWithConsortia(projects, filters.getConsortiaNames());
        }
        if (isValidList(filters.getPrivaciesNames()))
        {
            filteredResults = filterWithPrivacy(projects, filters.getPrivaciesNames());
        }
        return filteredResults;
    }

    private List<Project> filterWithTpn(List<Project> projects, List<String> values)
    {
        return projects.stream()
            .filter(x -> projectPredicates.inTpn(x, values))
            .collect(Collectors.toList());
    }

    private List<Project> filterWithWorkUnitName(List<Project> projects, List<String> values)
    {
        return projects.stream()
            .filter(result ->
                projectPredicates.inWorkGroupName(result, values))
            .collect(Collectors.toList());
    }

    private List<Project> filterWithWorkGroupName(
        List<Project> projects, List<String> values)
    {
        return projects.stream()
            .filter(result ->
                projectPredicates.inWorkUnitName(result, values))
            .collect(Collectors.toList());
    }

    private List<Project> filterWithExternalReference(
        List<Project> results, List<String> values)
    {
        return results.stream()
            .filter(result ->
                projectPredicates.inExternalReference(result, values))
            .collect(Collectors.toList());
    }

    private List<Project> filterWithIntentions(
        List<Project> results, List<String> values)
    {
        return results.stream()
            .filter(result ->
                projectPredicates.inIntentions(result, values))
            .collect(Collectors.toList());
    }

    private List<Project> filterWithConsortia(
        List<Project> results, List<String> values)
    {
        return results.stream()
            .filter(x -> projectPredicates.inConsortiaNames(x, values))
            .collect(Collectors.toList());
    }

    private List<Project> filterWithPrivacy(
        List<Project> results, List<String> values)
    {
        return results.stream()
            .filter(x -> projectPredicates.inPrivaciesNames(x, values))
            .collect(Collectors.toList());
    }

    private boolean isValidList(List<String> values)
    {
        return values != null && !values.isEmpty();
    }
}
