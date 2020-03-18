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

import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.organization.role.RoleService;
import org.gentar.organization.consortium.ConsortiumService;
import org.springframework.stereotype.Component;
import org.gentar.organization.person.associations.PersonRoleConsortium;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class PersonRoleConsortiumMapper implements Mapper<PersonRoleConsortium, PersonRoleConsortiumDTO>
{
    private ConsortiumService consortiumService;
    private RoleService roleService;
    private EntityMapper entityMapper;

    public PersonRoleConsortiumMapper(ConsortiumService consortiumService, RoleService roleService, EntityMapper entityMapper)
    {
        this.consortiumService = consortiumService;
        this.roleService = roleService;
        this.entityMapper = entityMapper;
    }

    @Override
    public PersonRoleConsortiumDTO toDto(PersonRoleConsortium entity) {
        PersonRoleConsortiumDTO personRoleConsortiumDTO = entityMapper.toTarget(entity, PersonRoleConsortiumDTO.class);
        personRoleConsortiumDTO.setId(entity.getId());
        personRoleConsortiumDTO.setConsortiumName(entity.getConsortium().getName());
        personRoleConsortiumDTO.setRoleName(entity.getRole().getName());

        return personRoleConsortiumDTO;
    }

    public PersonRoleConsortium toEntity(PersonRoleConsortiumDTO personRoleConsortiumDTO)
    {
        PersonRoleConsortium personRoleConsortium = new PersonRoleConsortium();
        personRoleConsortium.setId(getId(personRoleConsortiumDTO));
        String consortiumName = personRoleConsortiumDTO.getConsortiumName();
        personRoleConsortium.setConsortium(
            consortiumService.getConsortiumByNameOrThrowException(consortiumName));
        String roleName = personRoleConsortiumDTO.getRoleName();
        personRoleConsortium.setRole(roleService.getRoleByNameOrThrowException(roleName));
        return personRoleConsortium;
    }

    private Long getId(PersonRoleConsortiumDTO personRoleConsortiumDTO)
    {
        Long id = null;
        Long dtoId = personRoleConsortiumDTO.getId();
        if (dtoId != null)
        {
            id = dtoId  > 0 ? null : dtoId;
        }
        return id;
    }

    public Set<PersonRoleConsortium> toEntities(
        Collection<PersonRoleConsortiumDTO> personRoleConsortiumDTOS)
    {
        Set<PersonRoleConsortium> personRoleConsortia = new HashSet<>();
        if (personRoleConsortiumDTOS != null)
        {
            personRoleConsortiumDTOS.forEach(x -> personRoleConsortia.add(toEntity(x)));
        }
        return personRoleConsortia;
    }
}
