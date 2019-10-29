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
package uk.ac.ebi.impc_prod_tracker.service.organization;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.exceptions.UserOperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.Role;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.RoleRepository;
import java.util.List;

@Component
public class RoleService
{
    public static final String MANAGER_ROLE = "manager";
    public static final String GENERAL_ROLE = "general";

    private static final String ROLE_NOT_EXISTS_ERROR = "Role %s does not exist.";

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles()
    {
        return roleRepository.findAll();
    }

    @Cacheable("roleNames")
    public Role getRoleByName(String name)
    {
        return roleRepository.findRoleByName(name);
    }

    public Role getRoleByNameOrThrowException(String roleName)
    {
        Role role = getRoleByName(roleName);
        if (role == null)
        {
            throw new UserOperationFailedException(
                String.format(ROLE_NOT_EXISTS_ERROR, roleName));
        }
        return role;
    }
}
