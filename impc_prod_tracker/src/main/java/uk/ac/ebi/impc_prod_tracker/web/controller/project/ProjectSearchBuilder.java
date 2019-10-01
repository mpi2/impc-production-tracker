package uk.ac.ebi.impc_prod_tracker.web.controller.project;

import org.springframework.security.core.parameters.P;

import java.util.List;

public class ProjectSearchBuilder
{
    private List<String> tpns;
    private List<String> markerSymbols;
    private List<String> workUnitNames;
    private List<String> consortiaNames;
    private List<String> statusesNames;
    private List<String> privaciesNames;

    // Prevents instantiation.
    private ProjectSearchBuilder()
    {
    }

    public static ProjectSearchBuilder getInstance()
    {
        return new ProjectSearchBuilder();
    }

    public ProjectSearch build()
    {
        ProjectSearch projectSearch = new ProjectSearch();
        projectSearch.setTpns(tpns);
        projectSearch.setMarkerSymbols(markerSymbols);
        return projectSearch;
    }

    public ProjectSearchBuilder withTpns(List<String> tpns)
    {
        this.tpns = tpns;
        return this;
    }

    public ProjectSearchBuilder withMarkerSymbols(List<String> markerSymbols)
    {
        this.markerSymbols = markerSymbols;
        return this;
    }
}
