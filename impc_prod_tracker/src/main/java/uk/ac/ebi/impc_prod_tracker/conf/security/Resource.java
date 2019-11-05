package uk.ac.ebi.impc_prod_tracker.conf.security;

import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;

import java.util.List;

public interface Resource<T>
{
    ResourcePrivacy getResourcePrivacy();

    Resource<T> getRestrictedObject();

    List<WorkUnit> getRelatedWorkUnits();

    List<Consortium> getRelatedConsortia();
}
