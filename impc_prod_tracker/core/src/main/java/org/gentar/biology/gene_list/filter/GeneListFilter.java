package org.gentar.biology.gene_list.filter;

import lombok.Getter;
import lombok.Setter;
import org.gentar.biology.project.search.filter.FilterTypes;

import java.util.List;
import java.util.Map;

@Setter
public class GeneListFilter
{
    private Map<FilterTypes, List<String>> filters;

    @Getter
    private String consortiumName;
    private Boolean visible;

    public static GeneListFilter getInstance()
    {
        return new GeneListFilter();
    }

    public Boolean getVisible()
    {
        Boolean result = null;
        List<String> valuesAsString = filters.getOrDefault(FilterTypes.VISIBLE, null);
        if (valuesAsString != null && !valuesAsString.isEmpty())
        {
            result = Boolean.parseBoolean(valuesAsString.getFirst());
        }
        return result;
    }

    public List<String> getAccIds()
    {
        return filters.getOrDefault(FilterTypes.ACC_ID, null);
    }

}
