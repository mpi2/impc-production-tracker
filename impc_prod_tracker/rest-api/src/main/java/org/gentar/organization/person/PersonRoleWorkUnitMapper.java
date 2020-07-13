package org.gentar.organization.person;

import org.gentar.Mapper;
import org.gentar.organization.work_unit.WorkUnitService;
import org.gentar.organization.role.RoleService;
import org.springframework.stereotype.Component;
import org.gentar.organization.person.associations.PersonRoleWorkUnit;

@Component
public class PersonRoleWorkUnitMapper implements Mapper<PersonRoleWorkUnit, PersonRoleWorkUnitDTO>
{
    private final WorkUnitService workUnitService;
    private final RoleService roleService;

    public PersonRoleWorkUnitMapper(WorkUnitService workUnitService, RoleService roleService)
    {
        this.workUnitService = workUnitService;
        this.roleService = roleService;
    }

    @Override
    public PersonRoleWorkUnitDTO toDto(PersonRoleWorkUnit personRoleWorkUnit) {
        PersonRoleWorkUnitDTO personRoleWorkUnitDTO = new PersonRoleWorkUnitDTO();
        personRoleWorkUnitDTO.setId(personRoleWorkUnit.getId());
        personRoleWorkUnitDTO.setWorkUnitName(personRoleWorkUnit.getWorkUnit().getName());
        personRoleWorkUnitDTO.setRoleName(personRoleWorkUnit.getRole().getName());

        return personRoleWorkUnitDTO;
    }

    public PersonRoleWorkUnit toEntity(PersonRoleWorkUnitDTO personRoleWorkUnitDTO)
    {
        PersonRoleWorkUnit personRoleWorkUnit = new PersonRoleWorkUnit();
        personRoleWorkUnit.setId(personRoleWorkUnitDTO.getId());
        String workUnitName = personRoleWorkUnitDTO.getWorkUnitName();
        personRoleWorkUnit.setWorkUnit(
            workUnitService.getWorkUnitByNameOrThrowException(workUnitName));
        String roleName = personRoleWorkUnitDTO.getRoleName();
        personRoleWorkUnit.setRole(roleService.getRoleByNameOrThrowException(roleName));
        return personRoleWorkUnit;
    }
}
