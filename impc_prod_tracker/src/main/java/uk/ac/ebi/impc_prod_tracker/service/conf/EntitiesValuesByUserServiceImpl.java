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
package uk.ac.ebi.impc_prod_tracker.service.conf;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.SystemSubject;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.data.organization.consortium.Consortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.Institute;
import uk.ac.ebi.impc_prod_tracker.data.organization.person_role_consortium.PersonRoleConsortium;
import uk.ac.ebi.impc_prod_tracker.data.organization.person_role_work_unit.PersonRoleWorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.Role;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.service.InstituteService;
import uk.ac.ebi.impc_prod_tracker.service.RoleService;
import uk.ac.ebi.impc_prod_tracker.service.WorkUnitService;
import uk.ac.ebi.impc_prod_tracker.service.consortium.ConsortiumService;
import uk.ac.ebi.impc_prod_tracker.web.dto.common.NamedValueDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EntitiesValuesByUserServiceImpl implements EntitiesValuesByUserService
{
    private SubjectRetriever subjectRetriever;
    private WorkUnitService workUnitService;
    private ConsortiumService consortiumService;
    private RoleService roleService;
    private InstituteService instituteService;

    public EntitiesValuesByUserServiceImpl(
        SubjectRetriever subjectRetriever,
        WorkUnitService workUnitService,
        ConsortiumService consortiumService,
        RoleService roleService,
        InstituteService instituteService)
    {
        this.subjectRetriever = subjectRetriever;
        this.workUnitService = workUnitService;
        this.consortiumService = consortiumService;
        this.roleService = roleService;
        this.instituteService = instituteService;
    }

    @Override
    public List<EntityValues> getListsByManagerUser()
    {
        SystemSubject systemSubject = subjectRetriever.getSubject();
        List<EntityValues> entities = new ArrayList<>();
        entities.add(getWorkUnits(systemSubject));
        entities.add(getConsortia(systemSubject));
        entities.add(getInstitutes(systemSubject));
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
            workUnits = getManagedWorkUnits(systemSubject);
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
            consortia = getManagedConsortia(systemSubject);
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

    private EntityValues getInstitutes(SystemSubject systemSubject)
    {
        EntityValues entityValues = new EntityValues();
        List<Institute> institutes;
        if (systemSubject.isAdmin())
        {
            institutes = getAllInstitutes();
        }
        else
        {
            institutes = getManagedInstitutes(systemSubject);
        }
        List<NamedValueDTO> instituteNames =
            institutes.stream()
                .map(x -> new NamedValueDTO(x.getName()))
                .collect(Collectors.toList());
        entityValues.setEntityName("institutes");
        entityValues.setValues(instituteNames);
        return entityValues;
    }

    private List<WorkUnit> getManagedWorkUnits(SystemSubject systemSubject)
    {
        List<PersonRoleWorkUnit> personRoleWorkUnits = systemSubject.getRoleWorkUnits();
        return
            personRoleWorkUnits.stream()
                .filter(x -> RoleService.MANAGER_ROLE.equalsIgnoreCase(x.getRole().getName()))
                .map(PersonRoleWorkUnit::getWorkUnit)
                .collect(Collectors.toList());
    }

    private List<WorkUnit> getAllWorkUnits()
    {
        return workUnitService.getAllWorkUnits();
    }

    private List<Consortium> getManagedConsortia(SystemSubject systemSubject)
    {
        List<PersonRoleConsortium> personRoleConsortia = systemSubject.getRoleConsortia();
        return
            personRoleConsortia.stream()
                .filter(x -> RoleService.MANAGER_ROLE.equalsIgnoreCase(x.getRole().getName()))
                .map(PersonRoleConsortium::getConsortium)
                .collect(Collectors.toList());
    }

    private List<Consortium> getAllConsortia()
    {
        return consortiumService.findAllConsortia();
    }

    private List<Institute> getManagedInstitutes(SystemSubject systemSubject)
    {
        List<PersonRoleConsortium> personRoleConsortia = systemSubject.getRoleConsortia();
        return
            personRoleConsortia.stream()
                .filter(x -> RoleService.MANAGER_ROLE.equalsIgnoreCase(x.getRole().getName()))
                .map(PersonRoleConsortium::getInstitute)
                .collect(Collectors.toList());
    }

    private List<Institute> getAllInstitutes()
    {
        return instituteService.getAllInstitutes();
    }

    private List<Role> getAllRoles()
    {
        return roleService.getAllRoles();
    }
}
