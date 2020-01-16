package org.gentar.biology.project.search.filter;

import org.gentar.biology.project.Project;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FluentProjectFilter extends AbstractFluentProjectFilter<Project>
{
    private List<Project> data;

    public FluentProjectFilter(List<Project> data)
    {
        super(data);
        this.data = new ArrayList<>(data);
    }

    protected List<Project> withCondition(Predicate<Project> condition)
    {
        return data.stream()
            .filter(condition)
            .collect(Collectors.toList());
    }
}
