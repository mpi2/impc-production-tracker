package org.gentar.organization.role;

import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleServiceImpl implements RoleService
{
    public static final String MANAGER_ROLE = "manager";
    public static final String GENERAL_ROLE = "general";

    private static final String ROLE_NOT_EXISTS_ERROR = "Role %s does not exist.";

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository)
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
