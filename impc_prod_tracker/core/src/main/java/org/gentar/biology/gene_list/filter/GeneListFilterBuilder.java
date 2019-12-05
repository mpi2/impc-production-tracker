package org.gentar.biology.gene_list.filter;

import org.gentar.biology.project.search.filter.FilterTypes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneListFilterBuilder
{
    private Map<FilterTypes, List<String>> filters;
    private String consortiumName;

    // Prevents instantiation.
    private GeneListFilterBuilder()
    {
        filters = new HashMap<>();
    }

    public static GeneListFilterBuilder getInstance()
    {
        return new GeneListFilterBuilder();
    }

    public GeneListFilter build()
    {
        GeneListFilter geneListFilter = new GeneListFilter();
        geneListFilter.setFilters(filters);
        geneListFilter.setConsortiumName(consortiumName);
        return geneListFilter;
    }

    public GeneListFilterBuilder withConsortiumName(String consortiumName)
    {
        this.consortiumName = consortiumName;
        return this;
    }

    public GeneListFilterBuilder withAccIds(List<String> accIds)
    {
        filters.put(FilterTypes.ACC_ID, accIds);
        return this;
    }
}
