package uk.ac.ebi.impc_prod_tracker.service.conf;

import java.util.Map;

/**
 * Returns the permissions a user has in the application.
 */
public interface PermissionService
{
    /**
     * Get the general permissions a user have that don't depend on specific resources.
     * @return Map with the permissions.
     */
    Map<String, Boolean> getPermissions();

    /**
     * Returns if the current use can execute an comment on a specific resource.
     * @param action The comment to evaluate.
     * @param resourceId The resource identifier.
     * @return True if the use has permission to execute the comment.
     */
    boolean getPermissionByActionOnResource(String action, String resourceId);
}
