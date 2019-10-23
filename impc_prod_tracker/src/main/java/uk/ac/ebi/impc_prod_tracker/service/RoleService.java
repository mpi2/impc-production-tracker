package uk.ac.ebi.impc_prod_tracker.service;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.Role;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.RoleRepository;

import java.util.List;

@Component
public class RoleService
{
    public static final String MANAGER_ROLE = "manager";
    public static final String GENERAL_ROLE = "general";

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles()
    {
        return roleRepository.findAll();
    }
}
