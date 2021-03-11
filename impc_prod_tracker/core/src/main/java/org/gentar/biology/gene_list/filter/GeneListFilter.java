package org.gentar.biology.gene_list.filter;

import org.gentar.biology.project.search.filter.FilterTypes;

import java.util.List;
import java.util.Map;

public class GeneListFilter
{
    private Map<FilterTypes, List<String>> filters;

    private String consortiumName;
    private Boolean visible;

    public static GeneListFilter getInstance()
    {
        return new GeneListFilter();
    }

    public String getConsortiumName()
    {
        return consortiumName;
    }

    public Boolean getVisible()
    {
        Boolean result = null;
        List<String> valuesAsString = filters.getOrDefault(FilterTypes.VISIBLE, null);
        if (valuesAsString != null && !valuesAsString.isEmpty())
        {
            result = Boolean.parseBoolean(valuesAsString.get(0));
        }
        return result;
    }

    public List<String> getAccIds()
    {
        return filters.getOrDefault(FilterTypes.ACC_ID, null);
    }

    public void setConsortiumName(String consortiumName)
    {
        this.consortiumName = consortiumName;
    }

    public void setVisible(Boolean visible)
    {
        this.visible = visible;
    }

    public void setFilters(Map<FilterTypes, List<String>> filters)
    {
        this.filters = filters;
    }
}
