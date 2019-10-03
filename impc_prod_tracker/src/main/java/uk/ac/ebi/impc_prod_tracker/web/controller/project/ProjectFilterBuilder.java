package uk.ac.ebi.impc_prod_tracker.web.controller.project;

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

    static ProjectFilterBuilder getInstance()
    {
        return new ProjectFilterBuilder();
    }

    ProjectFilter build()
    {
        ProjectFilter projectFilter = new ProjectFilter();
        projectFilter.setTpns(tpns);
        projectFilter.setMarkerSymbols(markerSymbols);
        projectFilter.setWorkUnitNames(workUnitNames);
        projectFilter.setIntentions(intentions);
        projectFilter.setPrivaciesNames(privaciesNames);
        return projectFilter;
    }

    ProjectFilterBuilder withTpns(List<String> tpns)
    {
        this.tpns = tpns;
        return this;
    }

    ProjectFilterBuilder withMarkerSymbols(List<String> markerSymbols)
    {
        this.markerSymbols = markerSymbols;
        return this;
    }

    ProjectFilterBuilder withIntentions(List<String> intentions)
    {
        this.intentions = intentions;
        return this;
    }

    ProjectFilterBuilder withPrivacies(List<String> privacies)
    {
        this.privaciesNames = privacies;
        return this;
    }

    ProjectFilterBuilder withWorkUnitNames(List<String> workUnitNames)
    {
        this.workUnitNames = workUnitNames;
        return this;
    }
}
