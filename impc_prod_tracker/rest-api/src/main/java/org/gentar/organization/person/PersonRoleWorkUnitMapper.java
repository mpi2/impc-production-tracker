package org.gentar.organization.person;

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.organization.work_unit.WorkUnitService;
import org.gentar.organization.role.RoleService;
import org.springframework.stereotype.Component;
import org.gentar.organization.person.associations.PersonRoleWorkUnit;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class PersonRoleWorkUnitMapper implements Mapper<PersonRoleWorkUnit, PersonRoleWorkUnitDTO>
{
    private WorkUnitService workUnitService;
    private RoleService roleService;
    private EntityMapper entityMapper;

    public PersonRoleWorkUnitMapper(WorkUnitService workUnitService, RoleService roleService, EntityMapper entityMapper)
    {
        this.workUnitService = workUnitService;
        this.roleService = roleService;
        this.entityMapper = entityMapper;
    }

    @Override
    public PersonRoleWorkUnitDTO toDto(PersonRoleWorkUnit entity) {
        PersonRoleWorkUnitDTO personRoleWorkUnitDTO = entityMapper.toTarget(entity, PersonRoleWorkUnitDTO.class);
        personRoleWorkUnitDTO.setId(entity.getId());
        personRoleWorkUnitDTO.setWorkUnitName(entity.getWorkUnit().getName());
        personRoleWorkUnitDTO.setRoleName(entity.getRole().getName());

        return personRoleWorkUnitDTO;
    }

    public PersonRoleWorkUnit toEntity(PersonRoleWorkUnitDTO personRoleWorkUnitDTO)
    {
        PersonRoleWorkUnit personRoleWorkUnit = new PersonRoleWorkUnit();
        personRoleWorkUnit.setId(getId(personRoleWorkUnitDTO));
        String workUnitName = personRoleWorkUnitDTO.getWorkUnitName();
        personRoleWorkUnit.setWorkUnit(
            workUnitService.getWorkUnitByNameOrThrowException(workUnitName));
        String roleName = personRoleWorkUnitDTO.getRoleName();
        personRoleWorkUnit.setRole(roleService.getRoleByNameOrThrowException(roleName));
        return personRoleWorkUnit;
    }

    private Long getId(PersonRoleWorkUnitDTO personRoleWorkUnitDTO)
    {
        Long id = null;
        Long dtoId = personRoleWorkUnitDTO.getId();
        if (dtoId != null)
        {
            id = dtoId  > 0 ? null : dtoId;
        }
        return id;
    }

    public Set<PersonRoleWorkUnit> toEntities(
        Collection<PersonRoleWorkUnitDTO> personRoleWorkUnitDTOS)
    {
        Set<PersonRoleWorkUnit> personRoleWorkUnits = new HashSet<>();
        if (personRoleWorkUnitDTOS != null)
        {
            personRoleWorkUnitDTOS.forEach(x -> personRoleWorkUnits.add(toEntity(x)));
        }
        return personRoleWorkUnits;
    }
}
