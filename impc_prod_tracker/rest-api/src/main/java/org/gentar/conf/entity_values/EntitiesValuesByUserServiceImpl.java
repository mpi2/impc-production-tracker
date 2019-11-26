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
package org.gentar.conf.entity_values;

import org.gentar.organization.work_unit.WorkUnitService;
import org.gentar.security.abac.subject.SubjectRetriever;
import org.gentar.security.abac.subject.SystemSubject;
import org.gentar.organization.role.RoleService;
import org.gentar.organization.consortium.ConsortiumService;
import org.gentar.organization.management.ManagementService;
import org.gentar.common.NamedValueDTO;
import org.springframework.stereotype.Component;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.role.Role;
import org.gentar.organization.work_unit.WorkUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntitiesValuesByUserServiceImpl implements EntitiesValuesByUserService
{
    private SubjectRetriever subjectRetriever;
    private WorkUnitService workUnitService;
    private ConsortiumService consortiumService;
    private RoleService roleService;
    private ManagementService managementService;

    public EntitiesValuesByUserServiceImpl(
        SubjectRetriever subjectRetriever,
        WorkUnitService workUnitService,
        ConsortiumService consortiumService,
        RoleService roleService,
        ManagementService managementService)
    {
        this.subjectRetriever = subjectRetriever;
        this.workUnitService = workUnitService;
        this.consortiumService = consortiumService;
        this.roleService = roleService;
        this.managementService = managementService;
    }

    @Override
    public List<EntityValues> getListsByManagerUser()
    {
        SystemSubject systemSubject = subjectRetriever.getSubject();
        List<EntityValues> entities = new ArrayList<>();
        entities.add(getWorkUnits(systemSubject));
        entities.add(getConsortia(systemSubject));
        entities.add(getRoles());
        return entities;
    }

    private EntityValues getWorkUnits(SystemSubject systemSubject)
    {
        EntityValues entityValues = new EntityValues();
        List<WorkUnit> workUnits;
        if (systemSubject.isAdmin())
        {
            workUnits = getAllWorkUnits();
        }
        else
        {
            workUnits = managementService.getManagedWorkUnits(systemSubject);
        }
        List<NamedValueDTO> workUnitNames =
            workUnits.stream()
                .map(x -> new NamedValueDTO(x.getName()))
                .collect(Collectors.toList());
        entityValues.setEntityName("workUnits");
        entityValues.setValues(workUnitNames);
        return entityValues;
    }

    private EntityValues getConsortia(SystemSubject systemSubject)
    {
        EntityValues entityValues = new EntityValues();
        List<Consortium> consortia;
        if (systemSubject.isAdmin())
        {
            consortia = getAllConsortia();
        }
        else
        {
            consortia = managementService.getManagedConsortia(systemSubject);
        }
        List<NamedValueDTO> consortiaNames =
            consortia.stream()
                .map(x -> new NamedValueDTO(x.getName()))
                .collect(Collectors.toList());
        entityValues.setEntityName("consortia");
        entityValues.setValues(consortiaNames);
        return entityValues;
    }

    private EntityValues getRoles()
    {
        EntityValues entityValues = new EntityValues();
        List<Role> roles = getAllRoles();
        List<NamedValueDTO> rolesNames =
            roles.stream()
                .map(x -> new NamedValueDTO(x.getName()))
                .collect(Collectors.toList());
        entityValues.setEntityName("roles");
        entityValues.setValues(rolesNames);
        return entityValues;
    }

    private List<WorkUnit> getAllWorkUnits()
    {
        return workUnitService.getAllWorkUnits();
    }

    private List<Consortium> getAllConsortia()
    {
        return consortiumService.findAllConsortia();
    }

    private List<Role> getAllRoles()
    {
        return roleService.getAllRoles();
    }
}
