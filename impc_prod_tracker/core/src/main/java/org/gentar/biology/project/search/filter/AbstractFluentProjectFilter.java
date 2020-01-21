package org.gentar.biology.project.search.filter;

import org.gentar.biology.project.Project;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractFluentProjectFilter<T>
{
    protected List<T> data;

    public AbstractFluentProjectFilter(List<T> data)
    {
        this.data = data;
    }

    protected abstract List<T> withCondition(Predicate<Project> condition);

    public AbstractFluentProjectFilter<T> withTpns(List<String> tpns)
    {
        if (isListValid(tpns))
        {
            data = withCondition(ProjectPredicates.inTpns(tpns));
        }
        return this;
    }

    public AbstractFluentProjectFilter<T> withWorkUnitNames(List<String> workUnitNames)
    {
        if (isListValid(workUnitNames))
        {
            data = withCondition(ProjectPredicates.inWorkUnitNames(workUnitNames));
        }
        return this;
    }

    public AbstractFluentProjectFilter<T> withWorkGroupNames(List<String> workGroupNames)
    {
        if (isListValid(workGroupNames))
        {
            data = withCondition(ProjectPredicates.inWorkGroupNames(workGroupNames));
        }
        return this;
    }

    public AbstractFluentProjectFilter<T> withExternalReference(List<String> externalReferences)
    {
        if (isListValid(externalReferences))
        {
            data = withCondition(ProjectPredicates.inExternalReferences(externalReferences));
        }
        return this;
    }

    public AbstractFluentProjectFilter<T> withMolecularMutationTypeNames(List<String> molecularMutationTypeNames)
    {
        if (isListValid(molecularMutationTypeNames))
        {
            data = withCondition(ProjectPredicates.inMolecularMutationTypeNames(molecularMutationTypeNames));
        }
        return this;
    }

    public AbstractFluentProjectFilter<T> withConsortiaNames(List<String> consortiaNames)
    {
        if (isListValid(consortiaNames))
        {
            data = withCondition(ProjectPredicates.inConsortiaNames(consortiaNames));
        }
        return this;
    }

    public AbstractFluentProjectFilter<T> withPrivaciesNames(List<String> privaciesNames)
    {
        if (isListValid(privaciesNames))
        {
            data = withCondition(ProjectPredicates.inPrivaciesNames(privaciesNames));
        }
        return this;
    }

    public List<T> getFilteredData()
    {
        return data;
    }

    private boolean isListValid(List<String> values)
    {
        return values != null && !values.isEmpty();
    }
}
