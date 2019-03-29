package uk.ac.ebi.impc_prod_tracker.service;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.Institute;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.InstituteRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.PersonRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.Role;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.RoleRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnitRepository;
import uk.ac.ebi.impc_prod_tracker.domain.login.UserRegisterRequest;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PersonService {
    private PersonRepository personRepository;
    private RoleRepository roleRepository;
    private WorkUnitRepository workUnitRepository;
    private InstituteRepository instituteRepository;

    public PersonService (PersonRepository personRepository, RoleRepository roleRepository, WorkUnitRepository workUnitRepository, InstituteRepository instituteRepository)
    {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.workUnitRepository = workUnitRepository;
        this.instituteRepository = instituteRepository;
    }


    public Person createPerson (UserRegisterRequest userRegisterRequest)
    {
        String email = userRegisterRequest.getEmail();

        Person person = personRepository.findPersonByEmail(email);
        Role role = roleRepository.findRoleByName(userRegisterRequest.getRoleName());
        WorkUnit workUnit = workUnitRepository.findWorkUnitByName(userRegisterRequest.getWorkUnitName());
        Institute institute = instituteRepository.findInstituteByName(userRegisterRequest.getInstituteName());

        if (person == null)
        {
            person = new Person(email);
            person.setIsActive(true);
            person.setRole(role);
            person.setWorkUnit(workUnit);
            person.setInstitutes(Stream.of(institute).collect(Collectors.toSet()));

            personRepository.save(person);

        }
        return person;
    }
}
