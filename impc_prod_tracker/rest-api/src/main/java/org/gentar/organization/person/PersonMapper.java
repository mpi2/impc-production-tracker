/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.organization.person;

import org.gentar.security.permissions.ActionPermission;
import org.gentar.security.permissions.PermissionService;
import org.gentar.EntityMapper;
import org.springframework.stereotype.Component;
import org.gentar.organization.person.associations.PersonRoleConsortium;
import org.gentar.organization.person.associations.PersonRoleWorkUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class PersonMapper
{
    private EntityMapper entityMapper;
    private PermissionService permissionService;
    private PersonRoleWorkUnitMapper personRoleWorkUnitMapper;
    private PersonRoleConsortiumMapper personRoleConsortiumMapper;

    public PersonMapper(
        EntityMapper entityMapper,
        PermissionService permissionService,
        PersonRoleWorkUnitMapper personRoleWorkUnitMapper,
        PersonRoleConsortiumMapper personRoleConsortiumMapper)
    {
        this.entityMapper = entityMapper;
        this.permissionService = permissionService;
        this.personRoleWorkUnitMapper = personRoleWorkUnitMapper;
        this.personRoleConsortiumMapper = personRoleConsortiumMapper;
    }

    public PersonDTO toDto(Person person)
    {
        PersonDTO personDTO = entityMapper.toTarget(person, PersonDTO.class);
        if (personDTO != null)
        {
            personDTO.setRolesWorkUnits(peopleRoleWorkUnitToDtos(person.getRolesWorkUnits()));
            personDTO.setRolesConsortia(peopleRoleConsortiaToDto(person.getRolesConsortia()));
            List<ActionPermission> actionPermissions = permissionService.getPermissions();
            List<ActionPermissionDTO> actionPermissionDTOS = new ArrayList<>();
            if (actionPermissions != null)
            {
                actionPermissions.forEach(x -> {
                    ActionPermissionDTO actionPermissionDTO = new ActionPermissionDTO();
                    actionPermissionDTO.setActionName(x.getActionName());
                    actionPermissionDTO.setValue(x.isValue());
                    actionPermissionDTOS.add(actionPermissionDTO);
                });
            }
            personDTO.setActionPermissions(actionPermissionDTOS);
        }
        personDTO.setPassword(null);
        return personDTO;
    }

    public List<PersonDTO> toDtos(Collection<Person> people)
    {
        List<PersonDTO> personDTOS = new ArrayList<>();
        if (people != null)
        {
            people.forEach(x -> personDTOS.add(toDto(x)));
        }
        return personDTOS;
    }

    private PersonRoleWorkUnitDTO personRoleWorkUnitToDto(PersonRoleWorkUnit personRoleWorkUnit)
    {
        return entityMapper.toTarget(personRoleWorkUnit, PersonRoleWorkUnitDTO.class);
    }

    private List<PersonRoleWorkUnitDTO> peopleRoleWorkUnitToDtos(
        Collection<PersonRoleWorkUnit> peopleRoleWorkUnit)
    {
        return entityMapper.toTargets(peopleRoleWorkUnit, PersonRoleWorkUnitDTO.class);
    }

    private PersonRoleConsortiumDTO personRoleConsortiumToDto(PersonRoleConsortium personRoleConsortium)
    {
        return entityMapper.toTarget(personRoleConsortium, PersonRoleConsortiumDTO.class);
    }

    private List<PersonRoleConsortiumDTO> peopleRoleConsortiaToDto(
        Collection<PersonRoleConsortium> peopleRoleConsortia)
    {
        return entityMapper.toTargets(peopleRoleConsortia, PersonRoleConsortiumDTO.class);
    }

    public Person toEntity(PersonDTO personDTO)
    {
        Person person = new Person();
        person.setId(personDTO.getId());
        person.setName(personDTO.getName());
        person.setEmail(personDTO.getEmail());
        person.setPassword(personDTO.getPassword());
        person.setIsActive(true);
        person.setEbiAdmin(personDTO.isEbiAdmin());
        person.setContactable(personDTO.getContactable());
        addRolesWorkUnits(personDTO, person);
        addRolesConsortia(personDTO, person);

        return person;
    }

    public Person personCreationDTOtoEntity(PersonCreationDTO personCreationDTO)
    {
        Person person = new Person();
        person.setId(null);
        person.setName(personCreationDTO.getName());
        person.setEmail(personCreationDTO.getEmail());
        person.setPassword(personCreationDTO.getPassword());
        person.setIsActive(true);
        person.setEbiAdmin(personCreationDTO.isEbiAdmin());
        person.setContactable(personCreationDTO.getContactable());
        addRolesWorkUnits(personCreationDTO, person);
        addRolesConsortia(personCreationDTO, person);

        return person;
    }

    private void addRolesWorkUnits(PersonDTO personDTO, Person person)
    {
        List<PersonRoleWorkUnitDTO> roleWorkUnitDTOS = personDTO.getRolesWorkUnits();
        Set<PersonRoleWorkUnit> roleWorkUnitDTOSet =
            personRoleWorkUnitMapper.toEntities(roleWorkUnitDTOS);
        roleWorkUnitDTOSet.forEach(x -> x.setPerson(person));
        person.setRolesWorkUnits(roleWorkUnitDTOSet);
    }

    private void addRolesConsortia(PersonDTO personDTO, Person person)
    {
        List<PersonRoleConsortiumDTO> roleConsortiaDTOs = personDTO.getRolesConsortia();
        Set<PersonRoleConsortium> roleConsortiumSet =
            personRoleConsortiumMapper.toEntities(roleConsortiaDTOs);
        roleConsortiumSet.forEach(x -> x.setPerson(person));
        person.setRolesConsortia(roleConsortiumSet);
    }

    private void addRolesWorkUnits(PersonCreationDTO personCreationDTO, Person person)
    {
        List<PersonRoleWorkUnitDTO> roleWorkUnitDTOS = personCreationDTO.getRolesWorkUnits();
        Set<PersonRoleWorkUnit> roleWorkUnitDTOSet =
            personRoleWorkUnitMapper.toEntities(roleWorkUnitDTOS);
        roleWorkUnitDTOSet.forEach(x -> x.setPerson(person));
        person.setRolesWorkUnits(roleWorkUnitDTOSet);
    }

    private void addRolesConsortia(PersonCreationDTO personCreationDTO, Person person)
    {
        List<PersonRoleConsortiumDTO> roleConsortiaDTOs = personCreationDTO.getRolesConsortia();
        Set<PersonRoleConsortium> roleConsortiumSet =
            personRoleConsortiumMapper.toEntities(roleConsortiaDTOs);
        roleConsortiumSet.forEach(x -> x.setPerson(person));
        person.setRolesConsortia(roleConsortiumSet);
    }
}
