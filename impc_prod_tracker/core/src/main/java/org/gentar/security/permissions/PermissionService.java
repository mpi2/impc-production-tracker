/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package org.gentar.security.permissions;

import org.gentar.security.permissions.ActionPermission;

import java.util.List;

/**
 * Returns the permissions a user has in the application.
 */
public interface PermissionService
{
    String MANAGE_USERS_ACTION = "MANAGE_USERS";
    String EXECUTE_MANAGER_TASKS_ACTION = "EXECUTE_MANAGER_TASKS";
    String UPDATE_PLAN_ACTION = "UPDATE_PLAN";
    String UPDATE_PROJECT_ACTION = "UPDATE_PROJECT";
    String UPDATE_USER = "UPDATE_USER";
    String MANAGE_GENE_LIST_ACTION = "MANAGE_GENE_LISTS";

    /**
     * Get the general permissions a user has that don't depend on specific resources.
     * The user is the currently logged user in the system.
     * @return List with the permissions.
     */
    List<ActionPermission> getPermissionsLoggedUser();

    /**
     * Get the general permissions a user have that don't depend on specific resources.
     * @return List with the permissions.
     */
    /**
     * Get the general permissions a user has that don't depend on specific resources.
     * @param userEmail The user email that idendifies the record in the system.
     * @return List with the permissions.
     */
    List<ActionPermission> getPermissionsByUser(String userEmail);

    /**
     * Returns if the current use can execute an comment on a specific resource.
     * @param action The comment to evaluate.
     * @param resourceId The resource identifier.
     * @return True if the use has permission to execute the comment.
     */
    boolean getPermissionByActionOnResource(String action, String resourceId);
}
