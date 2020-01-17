package org.gentar.biology.project.search.filter;

import org.gentar.biology.project.Project;
import org.gentar.biology.project.search.SearchResult;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FluentSearchResultFilter extends AbstractFluentProjectFilter<SearchResult>
{
    private List<SearchResult> data;

    public FluentSearchResultFilter(List<SearchResult> data)
    {
        super(data);
        this.data = data;
    }

    protected List<SearchResult> withCondition(Predicate<Project> condition)
    {
        return data.stream()
            .filter(x ->
                {
                    if (x.getProject() == null)
                    {
                        return false;
                    }
                    return condition.test(x.getProject());
                }
            )
            .collect(Collectors.toList());
    }
}
