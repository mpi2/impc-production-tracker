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

import org.gentar.Mapper;
import org.gentar.organization.role.RoleService;
import org.gentar.organization.consortium.ConsortiumService;
import org.springframework.stereotype.Component;
import org.gentar.organization.person.associations.PersonRoleConsortium;

@Component
public class PersonRoleConsortiumMapper implements Mapper<PersonRoleConsortium, PersonRoleConsortiumDTO>
{
    private final ConsortiumService consortiumService;
    private final RoleService roleService;

    public PersonRoleConsortiumMapper(ConsortiumService consortiumService, RoleService roleService)
    {
        this.consortiumService = consortiumService;
        this.roleService = roleService;
    }

    @Override
    public PersonRoleConsortiumDTO toDto(PersonRoleConsortium entity) {
        PersonRoleConsortiumDTO personRoleConsortiumDTO = new PersonRoleConsortiumDTO();
        personRoleConsortiumDTO.setId(entity.getId());
        personRoleConsortiumDTO.setConsortiumName(entity.getConsortium().getName());
        personRoleConsortiumDTO.setRoleName(entity.getRole().getName());
        return personRoleConsortiumDTO;
    }

    public PersonRoleConsortium toEntity(PersonRoleConsortiumDTO personRoleConsortiumDTO)
    {
        PersonRoleConsortium personRoleConsortium = new PersonRoleConsortium();
        personRoleConsortium.setId(personRoleConsortiumDTO.getId());
        String consortiumName = personRoleConsortiumDTO.getConsortiumName();
        personRoleConsortium.setConsortium(
            consortiumService.getConsortiumByNameOrThrowException(consortiumName));
        String roleName = personRoleConsortiumDTO.getRoleName();
        personRoleConsortium.setRole(roleService.getRoleByNameOrThrowException(roleName));
        return personRoleConsortium;
    }
}
