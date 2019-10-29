package uk.ac.ebi.impc_prod_tracker.web.mapping.person;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.organization.person_role_work_unit.PersonRoleWorkUnit;
import uk.ac.ebi.impc_prod_tracker.service.organization.RoleService;
import uk.ac.ebi.impc_prod_tracker.service.organization.WorkUnitService;
import uk.ac.ebi.impc_prod_tracker.web.dto.person.PersonRoleWorkUnitDTO;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class PersonRoleWorkUnitMapper
{
    private WorkUnitService workUnitService;
    private RoleService roleService;

    public PersonRoleWorkUnitMapper(WorkUnitService workUnitService, RoleService roleService)
    {
        this.workUnitService = workUnitService;
        this.roleService = roleService;
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
