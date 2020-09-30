package org.gentar.security.abac;

import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.work_unit.WorkUnit;
import java.util.List;

/**
 * Interface that defines the methods a resource in GenTaR needs to have. They are used to
 * check the access policies.
 * @param <T>
 */
public interface Resource<T>
{
    ResourcePrivacy getResourcePrivacy();

    /**
     * Returns the version of the object when the privacy is restricted and the logged user
     * does not have privileges to see the complete object.
     * @return A version of the object with less information than the original.
     */
    Resource<T> getRestrictedObject();

    /**
     * Get the work units that are linked to the resource (being through the project, or plan, etc)
     * @return List of work units.
     */
    List<WorkUnit> getRelatedWorkUnits();

    /**
     * Get the consortia that are linked to the resource (being through the project, or plan, etc)
     * @return List of consortia.
     */
    List<Consortium> getRelatedConsortia();
}
