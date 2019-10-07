package uk.ac.ebi.impc_prod_tracker.web.controller.project.helper;

import uk.ac.ebi.impc_prod_tracker.common.types.FilterTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectFilterBuilder
{
    private Map<FilterTypes, List<String>> filters;

    // Prevents instantiation.
    private ProjectFilterBuilder()
    {
        filters = new HashMap<>();
    }

    public static ProjectFilterBuilder getInstance()
    {
        return new ProjectFilterBuilder();
    }

    public ProjectFilter build()
    {
        ProjectFilter projectFilter = new ProjectFilter();
        projectFilter.setFilters(filters);
        return projectFilter;
    }

    public ProjectFilterBuilder withTpns(List<String> tpns)
    {
        filters.put(FilterTypes.TPN, tpns);
        return this;
    }

    public ProjectFilterBuilder withMarkerSymbols(List<String> markerSymbols)
    {
        filters.put(FilterTypes.MARKER_SYMBOL, markerSymbols);
        return this;
    }

    public ProjectFilterBuilder withIntentions(List<String> intentions)
    {
        filters.put(FilterTypes.INTENTION, intentions);
        return this;
    }

    public ProjectFilterBuilder withPrivacies(List<String> privacies)
    {
        filters.put(FilterTypes.PRIVACY_NAME, privacies);
        return this;
    }

    public ProjectFilterBuilder withWorkUnitNames(List<String> workUnitNames)
    {
        filters.put(FilterTypes.WORK_UNIT_NAME, workUnitNames);
        return this;
    }
}
