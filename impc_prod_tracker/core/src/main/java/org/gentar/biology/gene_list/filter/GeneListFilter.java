package org.gentar.biology.gene_list.filter;

import org.gentar.biology.project.search.filter.FilterTypes;

import java.util.List;
import java.util.Map;

public class GeneListFilter
{
    private Map<FilterTypes, List<String>> filters;

    private String consortiumName;

    public static final GeneListFilter getInstance()
    {
        return new GeneListFilter();
    }

    public String getConsortiumName()
    {
        return consortiumName;
    }
    public List<String> getAccIds()
    {
        return filters.getOrDefault(FilterTypes.ACC_ID, null);
    }

    public void setConsortiumName(String consortiumName)
    {
        this.consortiumName = consortiumName;
    }

    public void setFilters(Map<FilterTypes, List<String>> filters)
    {
        this.filters = filters;
    }
}
