package org.gentar.security.abac;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface ResourceAccessChecker<T>
{
    Resource<T> checkAccess(Resource<T> resource, String action);

    default List<? extends Resource> checkAccessForCollection(
        Collection<? extends Resource> resources, String action)
    {
        return resources.stream()
            .map(p -> checkAccess(p, action))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
