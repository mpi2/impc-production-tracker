package org.gentar.security.abac;

import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.work_unit.WorkUnit;
import java.util.List;

public interface Resource<T>
{
    ResourcePrivacy getResourcePrivacy();

    Resource<T> getRestrictedObject();

    List<WorkUnit> getRelatedWorkUnits();

    List<Consortium> getRelatedConsortia();
}
