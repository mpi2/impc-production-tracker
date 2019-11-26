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
package org.gentar.organization.management;

import org.gentar.organization.work_unit.WorkUnitService;
import org.gentar.security.abac.subject.AapSystemSubject;
import org.gentar.security.abac.subject.SystemSubject;
import org.gentar.organization.role.RoleService;
import org.springframework.stereotype.Component;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.person.Person;
import org.gentar.organization.person.PersonRepository;
import org.gentar.organization.person.associations.PersonRoleConsortium;
import org.gentar.organization.person.associations.PersonRoleWorkUnit;
import org.gentar.organization.work_unit.WorkUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ManagementService
{
    private PersonRepository personRepository;
    private WorkUnitService workUnitService;

    public ManagementService(PersonRepository personRepository, WorkUnitService workUnitService)
    {
        this.personRepository = personRepository;
        this.workUnitService = workUnitService;
    }

    public List<WorkUnit> getManagedWorkUnits(SystemSubject systemSubject)
    {
        List<PersonRoleWorkUnit> personRoleWorkUnits = systemSubject.getRoleWorkUnits();
        return
            personRoleWorkUnits.stream()
                .filter(x -> RoleService.MANAGER_ROLE.equalsIgnoreCase(x.getRole().getName()))
                .map(PersonRoleWorkUnit::getWorkUnit)
                .collect(Collectors.toList());
    }

    public List<Consortium> getManagedConsortia(SystemSubject systemSubject)
    {
        List<PersonRoleConsortium> personRoleConsortia = systemSubject.getRoleConsortia();
        return
            personRoleConsortia.stream()
                .filter(x -> RoleService.MANAGER_ROLE.equalsIgnoreCase(x.getRole().getName()))
                .map(PersonRoleConsortium::getConsortium)
                .collect(Collectors.toList());
    }

    /**
     * Gets the people (with 'general' role) associated to work units or consortia that the user manages.
     * @param systemSubject The user executing the request.
     * @return List of {@link Person} managed by @systemSubject.
     */
    public List<Person> getManagedPeople(SystemSubject systemSubject)
    {
        List<Person> allPeople = personRepository.findAll();
        List<Person> managedPeopleByConsortia = getManagedPeopleByConsortium(systemSubject, allPeople);
        List<Person> managedPeopleByWorkUnits = getManagedPeopleByWorkUnits(systemSubject, allPeople);
        Set<Person> managedPeopleNoDuplicated = new HashSet<>();
        managedPeopleNoDuplicated.addAll(managedPeopleByConsortia);
        managedPeopleNoDuplicated.addAll(managedPeopleByWorkUnits);
        return new ArrayList<>(managedPeopleNoDuplicated);
    }

    private List<Person> getManagedPeopleByConsortium(
        SystemSubject systemSubject, List<Person> allPeople)
    {
        List<Person> managedPeople = new ArrayList<>();
        List<Consortium> managedConsortia = getManagedConsortia(systemSubject);
        allPeople.forEach(p -> {
            if (isPersonGeneralMemberInConsortia(p, managedConsortia))
            {
                managedPeople.add(p);
            }
        });
        return managedPeople;
    }

    private boolean isPersonGeneralMemberInConsortia(Person person, List<Consortium> consortia)
    {
        SystemSubject personAsSystemSubject
            = new AapSystemSubject(personRepository, workUnitService, person);
        boolean result = false;
        var consortiaWhereIsGeneralMember =
            personAsSystemSubject.getFluentRoleConsortia()
                .whereUserHasRole(RoleService.GENERAL_ROLE)
                .getConsortia();
        if (consortiaWhereIsGeneralMember != null)
        {
            result = consortiaWhereIsGeneralMember.stream().anyMatch(consortia::contains);
        }
        return result;
    }

    private List<Person> getManagedPeopleByWorkUnits(
        SystemSubject systemSubject, List<Person> allPeople)
    {
        List<Person> managedPeople = new ArrayList<>();
        List<WorkUnit> managedWorkUnits = getManagedWorkUnits(systemSubject);
        allPeople.forEach(p -> {
            if (isPersonGeneralMemberInWorkUnits(p, managedWorkUnits))
            {
                managedPeople.add(p);
            }
        });
        return managedPeople;
    }

    private boolean isPersonGeneralMemberInWorkUnits(Person person, List<WorkUnit> workUnits)
    {
        SystemSubject personAsSystemSubject
            = new AapSystemSubject(personRepository, workUnitService, person);
        boolean result = false;
        var workUnitsWhereIsGeneralMember =
            personAsSystemSubject.getFluentRoleWorkUnits()
                .whereUserHasRole(RoleService.GENERAL_ROLE)
                .getWorkUnits();
        if (workUnitsWhereIsGeneralMember != null)
        {
            result = workUnitsWhereIsGeneralMember.stream().anyMatch(workUnits::contains);
        }
        return result;
    }
}
