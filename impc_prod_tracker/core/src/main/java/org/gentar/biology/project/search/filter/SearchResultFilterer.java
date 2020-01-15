package org.gentar.biology.project.search.filter;

import org.gentar.biology.project.search.SearchResult;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchResultFilterer
{
    private ProjectPredicates projectPredicates;

    public SearchResultFilterer(ProjectPredicates projectPredicates)
    {
        this.projectPredicates = projectPredicates;
    }

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
            .filter(x -> projectPredicates.inTpn(x.getProject(), values))
            .collect(Collectors.toList());
    }

    private List<SearchResult> filterWithWorkUnitName(List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(result ->
                projectPredicates.inWorkGroupName(result.getProject(), values))
            .collect(Collectors.toList());
    }

    private List<SearchResult> filterWithWorkGroupName(
        List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(result ->
                projectPredicates.inWorkUnitName(result.getProject(), values))
            .collect(Collectors.toList());
    }

    private List<SearchResult> filterWithExternalReference(
        List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(result ->
                projectPredicates.inExternalReference(result.getProject(), values))
            .collect(Collectors.toList());
    }

    private List<SearchResult> filterWithIntentions(
        List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(result ->
                projectPredicates.inIntentions(result.getProject(), values))
            .collect(Collectors.toList());
    }

    private List<SearchResult> filterWithConsortia(
        List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(x -> projectPredicates.inConsortiaNames(x.getProject(), values))
            .collect(Collectors.toList());
    }

    private List<SearchResult> filterWithPrivacy(
        List<SearchResult> results, List<String> values)
    {
        return results.stream()
            .filter(x -> projectPredicates.inPrivaciesNames(x.getProject(), values))
            .collect(Collectors.toList());
    }

    private boolean isValidList(List<String> values)
    {
        return values != null && !values.isEmpty();
    }
}
