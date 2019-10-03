package uk.ac.ebi.impc_prod_tracker.web.controller.project.helper;

import java.util.List;

public class ProjectFilterBuilder
{
    private List<String> tpns;
    private List<String> markerSymbols;
    private List<String> intentions;
    private List<String> workUnitNames;
    private List<String> consortiaNames;
    private List<String> statusesNames;
    private List<String> privaciesNames;

    // Prevents instantiation.
    private ProjectFilterBuilder()
    {
    }

    public static ProjectFilterBuilder getInstance()
    {
        return new ProjectFilterBuilder();
    }

    public ProjectFilter build()
    {
        ProjectFilter projectFilter = new ProjectFilter();
        projectFilter.setTpns(tpns);
        projectFilter.setMarkerSymbols(markerSymbols);
        projectFilter.setWorkUnitNames(workUnitNames);
        projectFilter.setIntentions(intentions);
        projectFilter.setPrivaciesNames(privaciesNames);
        return projectFilter;
    }

    public ProjectFilterBuilder withTpns(List<String> tpns)
    {
        this.tpns = tpns;
        return this;
    }

    public ProjectFilterBuilder withMarkerSymbols(List<String> markerSymbols)
    {
        this.markerSymbols = markerSymbols;
        return this;
    }

    public ProjectFilterBuilder withIntentions(List<String> intentions)
    {
        this.intentions = intentions;
        return this;
    }

    public ProjectFilterBuilder withPrivacies(List<String> privacies)
    {
        this.privaciesNames = privacies;
        return this;
    }

    public ProjectFilterBuilder withWorkUnitNames(List<String> workUnitNames)
    {
        this.workUnitNames = workUnitNames;
        return this;
    }
}
