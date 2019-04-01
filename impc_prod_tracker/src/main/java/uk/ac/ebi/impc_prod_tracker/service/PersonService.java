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
package uk.ac.ebi.impc_prod_tracker.service;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.exeption_management.OperationFailedException;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.Institute;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.InstituteRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.Person;
import uk.ac.ebi.impc_prod_tracker.data.organization.person.PersonRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.Role;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.RoleRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnit;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnitRepository;
import uk.ac.ebi.impc_prod_tracker.domain.login.UserRegisterRequest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service to manage the creation of an user in the system.
 */

@Component
public class PersonService
{
    private PersonRepository personRepository;
    private RoleRepository roleRepository;
    private WorkUnitRepository workUnitRepository;
    private InstituteRepository instituteRepository;

    private static final String PERSON_ALREADY_EXISTS_ERROR =
        "The user with email {%s} already exists in the system.";

    public PersonService(
        PersonRepository personRepository,
        RoleRepository roleRepository,
        WorkUnitRepository workUnitRepository,
        InstituteRepository instituteRepository)
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
        else
        {
            throw new OperationFailedException(String.format(PERSON_ALREADY_EXISTS_ERROR, email));
        }

        return person;
    }
}
