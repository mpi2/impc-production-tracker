package uk.ac.ebi.impc_prod_tracker.conf.security;

public interface Resource<T>
{
    ResourcePrivacy getResourcePrivacy();

    Resource<T> getRestrictedObject();
}
