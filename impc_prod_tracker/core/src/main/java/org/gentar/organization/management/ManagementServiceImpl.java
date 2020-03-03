package org.gentar.organization.management;

import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.person.Person;
import org.gentar.organization.person.PersonRepository;
import org.gentar.organization.person.associations.PersonRoleConsortium;
import org.gentar.organization.person.associations.PersonRoleWorkUnit;
import org.gentar.organization.role.RoleService;
import org.gentar.organization.role.RoleServiceImpl;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.organization.work_unit.WorkUnitService;
import org.gentar.security.abac.subject.AapSystemSubject;
import org.gentar.security.abac.subject.SystemSubject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ManagementServiceImpl implements ManagementService
{
    private PersonRepository personRepository;
    private WorkUnitService workUnitService;
    private RoleServiceImpl roleService;


    public ManagementServiceImpl(PersonRepository personRepository, WorkUnitService workUnitService, RoleServiceImpl roleService)
    {
        this.personRepository = personRepository;
        this.workUnitService = workUnitService;
        this.roleService = roleService;
    }

    public List<WorkUnit> getManagedWorkUnits(SystemSubject systemSubject)
    {
        List<PersonRoleWorkUnit> personRoleWorkUnits = systemSubject.getRoleWorkUnits();
        return
                personRoleWorkUnits.stream()
                        .filter(x -> roleService.MANAGER_ROLE.equalsIgnoreCase(x.getRole().getName()))
                        .map(PersonRoleWorkUnit::getWorkUnit)
                        .collect(Collectors.toList());
    }

    public List<Consortium> getManagedConsortia(SystemSubject systemSubject)
    {
        List<PersonRoleConsortium> personRoleConsortia = systemSubject.getRoleConsortia();
        return
                personRoleConsortia.stream()
                        .filter(x -> roleService.MANAGER_ROLE.equalsIgnoreCase(x.getRole().getName()))
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
                        .whereUserHasRole(roleService.GENERAL_ROLE)
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
                        .whereUserHasRole(roleService.GENERAL_ROLE)
                        .getWorkUnits();
        if (workUnitsWhereIsGeneralMember != null)
        {
            result = workUnitsWhereIsGeneralMember.stream().anyMatch(workUnits::contains);
        }
        return result;
    }
}
