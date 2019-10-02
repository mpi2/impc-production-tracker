package uk.ac.ebi.impc_prod_tracker.web.controller.project;

import org.springframework.security.core.parameters.P;

import java.util.List;

public class ProjectSearchBuilder
{
    private List<String> tpns;
    private List<String> markerSymbols;
    private List<String> intentions;
    private List<String> workUnitNames;
    private List<String> consortiaNames;
    private List<String> statusesNames;
    private List<String> privaciesNames;

    // Prevents instantiation.
    private ProjectSearchBuilder()
    {
    }

    static ProjectSearchBuilder getInstance()
    {
        return new ProjectSearchBuilder();
    }

    ProjectSearch build()
    {
        ProjectSearch projectSearch = new ProjectSearch();
        projectSearch.setTpns(tpns);
        projectSearch.setMarkerSymbols(markerSymbols);
        projectSearch.setIntentions(intentions);
        projectSearch.setPrivaciesNames(privaciesNames);
        return projectSearch;
    }

    ProjectSearchBuilder withTpns(List<String> tpns)
    {
        this.tpns = tpns;
        return this;
    }

    ProjectSearchBuilder withMarkerSymbols(List<String> markerSymbols)
    {
        this.markerSymbols = markerSymbols;
        return this;
    }

    ProjectSearchBuilder withIntentions(List<String> intentions)
    {
        this.intentions = intentions;
        return this;
    }

    ProjectSearchBuilder withPrivacies(List<String> privacies)
    {
        this.privaciesNames = privacies;
        return this;
    }
}
